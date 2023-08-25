# Warning_Sys
该系统为一个不合规行为（未带安全帽，抽烟，玩手机）预警和统计系统
系统模型如下图所示，行为检测模型检测到不合规行为时，将预警消息，发送到RocketMq，服务端监听消息主题，当有新的消息时，服务端接受新的消息，然后根据消息内容向有关人员发送预警信息，并且更新数据库中的预警信息，以及预警类型，违规行为发生场地，数据统计等信息。
![绘图1](https://github.com/codezjt/Warning_Sys/assets/60995778/205b55fd-7510-4a21-8137-b0006f0ad60a)
![0001](https://github.com/codezjt/Warning_Sys/assets/60995778/41ee7485-3697-4b64-b080-8ab575878d90)
![0002](https://github.com/codezjt/Warning_Sys/assets/60995778/786f2405-e55e-4d44-b7fb-ce2dea8e2e34)
![0003](https://github.com/codezjt/Warning_Sys/assets/60995778/1063d486-a6ef-473a-b3c8-de412e7ca185)
![0004](https://github.com/codezjt/Warning_Sys/assets/60995778/683e8a6c-3512-4929-806d-4682c73e7ab3)


