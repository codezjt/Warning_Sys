import cv2

import time



# 清空streams.txt文件
with open('/root/shzn/yolov5/streams.txt', 'w') as file:
    file.write('')

# 读取stream.txt中的rtsp地址
with open('/root/shzn/yolov5/stream.txt', 'r') as file:
    rtsp_addresses = file.read().splitlines()

# 测试每个rtsp地址是否能够成功读取流
for rtsp_address in rtsp_addresses:
    cap = cv2.VideoCapture(rtsp_address)
    start_time = time.time()
    
    while True:
        # 读取视频帧
        ret, frame = cap.read()
        
        # 如果成功读取到帧，则将该地址写入streams.txt文件
        if ret:
            with open('/root/shzn/yolov5/streams.txt', 'a') as file:
                file.write(rtsp_address + '\n')
                print(rtsp_address + ' success')
            break
        
        # 判断超时时间，如果超过5秒仍然没有读取到帧，则判定为无法拉取流
        elapsed_time = time.time() - start_time
        if elapsed_time > 3:
            print(f"无法从 {rtsp_address} 拉取流")
            break
    
    cap.release()



