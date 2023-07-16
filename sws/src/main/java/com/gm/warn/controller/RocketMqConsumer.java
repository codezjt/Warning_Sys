package com.gm.warn.controller;


import com.alibaba.fastjson.JSON;
import com.gm.warn.entity.Event;
import com.gm.warn.service.CaclService;
import com.gm.warn.service.EventService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

@Component
public class RocketMqConsumer implements MessageListenerConcurrently {

    @Autowired
    CaclService caclService;
    @Autowired
    EventService eventService;
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if(msgs.isEmpty()){
            System.out.println("MQ接收消息为空，直接返回成功");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt messageExt = msgs.get(0);
//        System.out.println("MQ接收到的消息为：" + messageExt.toString());
        String topic = messageExt.getTopic();
        String tags = messageExt.getTags();
//            String body = new String(messageExt.getBody(), "utf-8");
        byte[] body = messageExt.getBody();
        Event e = JSON.parseObject(body, Event.class);
        try {
            eventService.updateCacl(e);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }


//        System.out.println("MQ消息topic={}, tags={}, 消息内容={}" + topic+tags + String.valueOf(body));
//        System.out.println("MQ消息topic={}, tags={}, 消息内容={}" + topic+tags + e);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
