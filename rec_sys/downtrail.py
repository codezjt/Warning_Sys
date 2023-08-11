import datetime
import sys
import os
import threading
import time


import torch
import math
import numpy as np
from utils.torch_utils import select_device
from models.common import DetectMultiBackend
from pathlib import Path
from utils.general import check_img_size, clean_str, is_colab, is_kaggle, cv2, LOGGER, Profile, non_max_suppression, scale_boxes
from threading import Thread
from utils.augmentations import letterbox
from utils.plots import Annotator, colors
import json
from rocketmq.client import Producer, Message
FILE = Path(__file__).resolve()
ROOT = FILE.parents[0]  # YOLOv5 root directory
if str(ROOT) not in sys.path:
    sys.path.append(str(ROOT))  # add ROOT to PATH
ROOT = Path(os.path.relpath(ROOT, Path.cwd()))  # relative

weight = ROOT / 'weights/best_dress.pt'
is_interrupt = []
device = select_device('cpu')
conf_thres = 0.5
iou_thres = 0.45
max_det = 1000
line_thickness = 2
fnum = 5
fmt = '%Y-%m-%d %H:%M:%S'
channel_dir = '../xwsb/channel_img'

whole_class = ['no-workcloth', 'no-hemelt', 'smoke', 'areca', 'face', 'call', 'play', 'sleep']
lab = [1, 2, 3, 4, 5, 6, 7, 8]
type_dict = dict(zip(whole_class, lab))

rtsp_urls = [
    'rtsp://xxxx:xxxx@xxxx/Streaming/Channels/101'

]
rtsp_names = [
    '厂区西门入口',
    'A101柱子',
    'A102柱子',
    'A103柱子',
    'A104柱子',
    'A105柱子',
    'A106柱子',
    'A107柱子',
    'A108柱子',
    'A109柱子',
    'A110柱子',
    'B101柱子',
    'B102柱子',
    'B103柱子',
    'B104柱子',
    'B105柱子',
    'B106柱子',
    'B107柱子',
    'B108柱子',
    'B109柱子',
    'B110柱子',
    'C101柱子',
    'C102柱子',
    'C103柱子',
    'C104柱子',
    'C105柱子',
    'C106柱子',
    'C107柱子',
    'C108柱子',
    'C109柱子',
    'C110柱子',
    'D101柱子',
    'D102柱子',
    'D103柱子',
    'D104柱子',
    'D105柱子',
    'D106柱子',
    'D107柱子',
    'D108柱子',
    'D109柱子',
    'D110柱子',
    'A南调试场大门',
    'A北调试场大门',
    'B南调试场大门',
    'B北调试场大门',
    'C南调试场大门',
    'C北调试场大门',
    'D南调试场大门',
    'D北调试场大门',
    '厂区东门入口',
]
location_dict = dict(zip(rtsp_urls, rtsp_names))

class LoadStreams:
    # YOLOv5 streamloader, i.e. `python detect.py --source 'rtsp://example.com/media.mp4'  # RTSP, RTMP, HTTP streams`
    def __init__(self, sources='file.streams', img_size=640, stride=32, auto=True, transforms=None, vid_stride=1, t_id=None):
        torch.backends.cudnn.benchmark = True  # faster for fixed-size inference
        self.mode = 'stream'
        self.img_size = img_size
        self.stride = stride
        self.vid_stride = vid_stride  # video frame-rate stride
        sources = Path(sources).read_text().rsplit() if os.path.isfile(sources) else [sources]
        n = len(sources)
        self.sources = [clean_str(x) for x in sources]  # clean source names for later
        self.imgs, self.fps, self.frames, self.threads = [None] * n, [0] * n, [0] * n, [None] * n
        for i, s in enumerate(sources):  # index, source
            # Start thread to read frames from video stream
            st = f'{i + 1}/{n}: {s}... '
            s = eval(s) if s.isnumeric() else s  # i.e. s = '0' local webcam
            if s == 0:
                assert not is_colab(), '--source 0 webcam unsupported on Colab. Rerun command in a local environment.'
                assert not is_kaggle(), '--source 0 webcam unsupported on Kaggle. Rerun command in a local environment.'
            cap = cv2.VideoCapture(s)
            assert cap.isOpened(), f'{st}Failed to open {s}'
            w = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
            h = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
            fps = cap.get(cv2.CAP_PROP_FPS)  # warning: may return 0 or nan
            self.frames[i] = max(int(cap.get(cv2.CAP_PROP_FRAME_COUNT)), 0) or float('inf')  # infinite stream fallback
            self.fps[i] = max((fps if math.isfinite(fps) else 0) % 100, 0) or 30  # 30 FPS fallback

            _, self.imgs[i] = cap.read()  # guarantee first frame
            self.threads[i] = Thread(target=self.update, args=([i, cap, s, t_id]), daemon=True)
            LOGGER.info(f'{st} Success ({self.frames[i]} frames {w}x{h} at {self.fps[i]:.2f} FPS)')
            self.threads[i].start()
        LOGGER.info('')  # newline

        # check for common shapes
        s = np.stack([letterbox(x, img_size, stride=stride, auto=auto)[0].shape for x in self.imgs])
        self.rect = np.unique(s, axis=0).shape[0] == 1  # rect inference if all shapes equal
        self.auto = auto and self.rect
        self.transforms = transforms  # optional
        if not self.rect:
            LOGGER.warning('WARNING ⚠️ Stream shapes differ. For optimal performance supply similarly-shaped streams.')

    def update(self, i, cap, stream, id):
        # Read stream `i` frames in daemon thread
        n, f = 0, self.frames[i]  # frame number, frame array
        while cap.isOpened() and n < f and is_interrupt[id] == False:
            n += 1
            cap.grab()  # .read() = .grab() followed by .retrieve()
            if n % self.vid_stride == 0:
                success, im = cap.retrieve()
                if success:
                    self.imgs[i] = im
                else:
                    print(f'线程{id}在读流状态中中断，请查看摄像头IP是否可以正常连接...')
                    self.imgs[i] = np.zeros_like(self.imgs[i])
                    is_interrupt[id] = True
            time.sleep(0.0)  # wait time

    def __iter__(self):
        self.count = -1
        return self

    def __next__(self):
        self.count += 1
        if not all(x.is_alive() for x in self.threads) or cv2.waitKey(1) == ord('q'):  # q to quit
            cv2.destroyAllWindows()
            raise StopIteration

        im0 = self.imgs.copy()
        if self.transforms:
            im = np.stack([self.transforms(x) for x in im0])  # transforms
        else:
            im = np.stack([letterbox(x, self.img_size, stride=self.stride, auto=self.auto)[0] for x in im0])  # resize
            im = im[..., ::-1].transpose((0, 3, 1, 2))  # BGR to RGB, BHWC to BCHW
            im = np.ascontiguousarray(im)  # contiguous

        return self.sources, im, im0, None, ''
    def __len__(self):
        return len(self.sources)  # 1E12 frames = 32 streams at 30 FPS for 30 years

def send_msg(cover,name,parties,date,road,note,cid)
    max_retries = 3  # 设置最大重发次数
    retry_count = 0
    id = 0
    producer = Producer("mes_produce")
    producer.set_namesrv_addr("127.0.0.1:9876")
    producer.start()
    msg = Message('test-topic')
    msg.set_keys('warn_msg')
    msg_body = {"id": id, 'name': name, 'parties': parties, 'date': date, 'road': road, 'note': note, 'cover': cover, "iswarning": 0, 'cid': cid }
    body = json.dumps(msg_body).encode('utf-8')
    msg.set_body(body)
    while retry_count < max_retries:
        try:
            ret = producer.send_sync(msg)
            LOGGER.info("已发送预警消息：%s", body)
            break  # 发送成功，跳出循环
        except Exception as e:
            LOGGER.info("消息发送失败重试%s次：", retry_count + 1)
            retry_count += 1
    if retry_count == max_retries:
        LOGGER.info("消息发送失败达到最大重试次数，放弃发送：%s", body)
    producer.shutdown()

def con2msg_hnu(type, conf, vidcut_picture, rtsp_addr, t_id):
    con = f'{conf:.2f}'
    cover = vidcut_picture.replace('/data/release/file/vision/', '/file/')
    send_msg(cover, '人员不合规行为识别', con + '%', time.strftime(fmt, time.localtime()), location_dict[rtsp_addr], '', type_dict[type])


def t_model(model, rtsp_addr, t_id):
    is_interrupt[t_id] = False
    print(f'线程{t_id}已经启动...')
    try:
        dataset = LoadStreams(rtsp_addr, img_size=(640, 640), stride=32, auto=True, vid_stride=1, t_id=t_id)
        dt = (Profile(), Profile(), Profile())
        for path, im, im0s, vid_cap, s in dataset:
         if is_interrupt[t_id] == False:
            vidcut_dir = '/data/release/file/vision/' + datetime.datetime.now().strftime("%Y") + '/' + datetime.datetime.now().strftime("%m") + '/' + datetime.datetime.now().strftime("%d")
            if not os.path.exists(vidcut_dir):
                os.makedirs(vidcut_dir)
            with dt[0]:
                im = torch.from_numpy(im).to(device).float()
                im /= 255
                if len(im.shape) == 3:
                    im = im[None]
            with dt[1]:
                # 模型推理
                pred = model(im, augment=False, visualize=False)
            with dt[2]:
                pred = non_max_suppression(pred, conf_thres=conf_thres, iou_thres=iou_thres, classes=None, agnostic=False, max_det=max_det)
            for i, det in enumerate(pred):
                im0, frame = im0s[i].copy(), dataset.count
                s += f'{i}: '
                s += '%gx%g ' % im.shape[2:]
                annotator = Annotator(im0, line_width=line_thickness, example=str(model.names))
                if len(det):
                    channel_img = im0.copy()
                    for c in det[:, 5].unique():
                        n = (det[:, 5] == c).sum()
                        s += f"{n} {model.names[int(c)]}{'s' * (n > 1)}, "
                    det[:, :4] = scale_boxes(im.shape[2:], det[:, :4], im0.shape).round()
                    is_sql = [0] * len(model.names)
                    main_label = ''
                    max_conf = 0
                    for *xyxy, conf, cls in reversed(det):
                        c = int(cls)
                        label = f'{model.names[c]} {conf:.2f}'
                        annotator.box_label(xyxy, label, color=colors(c, True))
                        if conf > max_conf:
                            max_conf = conf
                            main_label = model.names[c]
                        if conf > is_sql[c]:
                            is_sql[c] = conf
                    # picture_name = main_label + '_' + str(t_id) +'_' + datetime.datetime.now().strftime("%Y%m%d%H%M%S") + 'A001'  + '.jpg'
                    picture_name = 'dress_' + str(t_id) + '_' + datetime.datetime.now().strftime("%Y%m%d%H%M%S") + 'A001' + '.jpg'
                    vidcut_picture = vidcut_dir + '/' + picture_name
                    a0 = (int(xyxy[0].item()) + int(xyxy[2].item())) / 2
                    if frame % fnum == 0 and a0 != 0 and max_conf >= 0.5:
                        cv2.imencode('.jpg', im0)[1].tofile(vidcut_picture)
                        cv2.imencode('.jpg', channel_img)[1].tofile(channel_dir + '/' + picture_name)
                        print(f'线程{t_id}：检测到高置信度图片，保存为：{vidcut_picture}')
                        for i in range(len(is_sql)):
                            if is_sql[i] >= 0.5 and datetime.time(7, 30) <= datetime.datetime.now().time() <= datetime.time(17, 30):
                                con2sql_hnu(model.names[i], is_sql[i] * 100, vidcut_picture, rtsp_addr, t_id)
            LOGGER.info(f"{str(t_id) + ':' + s}{'' if len(det) else '(no detections), '}{dt[1].dt * 1E3:.1f}ms")
         else:
            return
    except Exception as e:
        is_interrupt[t_id] = True
        print(e)


def main():
    global is_interrupt
    # 检测同级目录下是否有streams.txt并判断是否为空文件，只有该文件存在且非空才会运行识别程序
    try:
        with open('stream_human.txt', 'r') as f:
            rtsp_lines = f.readlines()
            if len(rtsp_lines) == 0:
                print("streams.txt是空文件，无法读取视频流！")
                exit(1)
            thread_num = len(rtsp_lines)
    except FileNotFoundError:
        print("找不到streams.txt文件，无法读取视频流！")
        exit(1)
    print(f"要求处理的视频流个数: {thread_num}")
    for lines in rtsp_lines:
        print(lines.strip())
    is_interrupt = [False] * thread_num
    # 加载模型
    model = DetectMultiBackend(weight, device=device, dnn= False, data=None, fp16 = False)
    print('Successfully load the models!')
    imgsz = check_img_size(())
    model.warmup(imgsz=(1, 3, *imgsz))
    print('successfully warm up model!')
    for i in range(thread_num):
        threading.Thread(target=t_model, args=(model, rtsp_lines[i].strip(), i)).start()
    while True:
        print(f'三分钟后开始检测线程状况...')
        time.sleep(180)
        print(f'开始检测线程状况...')
        print(f'当前线程状况为{is_interrupt}...')
        for i in range(thread_num):
            if is_interrupt[i] == True:
                print(f'检测到线程{i}已经中断，正在使线程{i}重启...')
                threading.Thread(target=t_model, args=(model, rtsp_lines[i].strip(), i)).start()
            else:
                print(f'线程{i}正在运行中...')

if __name__ == '__main__':
    main()
