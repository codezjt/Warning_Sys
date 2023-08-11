import cv2
import os

# RTSP流地址
rtsp_url = "rtsp://admin:sunward12345@6.0.0.13:10101/Streaming/Channels/101"

# 保存图片的目录
output_directory = "test_picture"

# 创建输出目录
os.makedirs(output_directory, exist_ok=True)

# 创建 OpenCV 的视频捕获对象
cap = cv2.VideoCapture(rtsp_url)

# 检查是否成功连接到流
if not cap.isOpened():
    print("Falsetoreal")
    exit()

# 读取一帧图像
ret, frame = cap.read()

# 检查是否成功读取到图像
if not ret:
    print("Falsetoreal")
    exit()

# 保存图像文件
output_file = os.path.join(output_directory, "captured_image.jpg")
cv2.imwrite(output_file, frame)

# 释放视频捕获对象和关闭所有窗口
cap.release()
cv2.destroyAllWindows()