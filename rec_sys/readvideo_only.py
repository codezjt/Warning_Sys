import cv2



rtsp_urls = [
    'rtsp://xxxx:xxxx@xxxx/Streaming/Channels/101'
]

for url in rtsp_urls:
    cap = cv2.VideoCapture(url)
    if cap.isOpened():
        print(url + ' success')
    else:
        print(url + ' failed')
    cap.release()



