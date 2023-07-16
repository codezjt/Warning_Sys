package com.gm.warn.config;


import com.gm.warn.entity.Event;
import com.gm.warn.redis.RedisService;
import com.gm.warn.service.EventService;
import com.gm.warn.service.warning.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    RedisService redisService;

    @Autowired
    EventService eventService;

    @Scheduled(cron = "0/1 * * * * ?")
    @Transactional
    void configureTasks() throws IOException {
        int currentId  = 0;
        int id = 1 ;
        String key = "currentid";
        Event eventwarring;
//        redisService = SpringContextUtils.getContext().getBean(RedisService.class);
//        eventService = SpringContextUtils.getContext().getBean(EventService.class);

            Object redisGetId = redisService.get(key);
//            System.out.println(redisGetId + "初始redisGetId+++++++++++++++++++++++");
            if(redisGetId == null)             //看redis中是否有存id值
            {
                redisService.set(key, 0);
            }
            else {
                currentId = (int)redisGetId;       //redis中有值的话赋值为当前id
//                System.out.println(currentId + "currentId+++++++++++++++++++++++");
//                System.out.println(redisGetId + "redisGetId+++++++++++++++++++++++");
            }
            List<Event> events = eventService.getAddEvents(currentId);
            if (events.size() != 0){
                for (Event event : events){
                        int eventid = event.getId();
                        String accuracy = event.getParties();
                        String road = event.getRoad();
                        String category = event.getCategory().getName(); //获取类别空指针异常
                        id = event.getId();
                        String accstring = accuracy.replace("%", "");
                        Double acc = Double.valueOf(accstring.toString());
                        if(event.getIswarning().length() == 0) {
                            if (acc >= 80) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间？？？？？
                                Date date = new Date(System.currentTimeMillis());
                                String time = formatter.format(date);
                                eventService.updateIsWarning(time, eventid);
                                eventwarring = eventService.getEventById(eventid);
//                        String WarringMsg = eventwarring.getRoad() + "摄像头在" + eventwarring.getDate() + "检测到" + eventwarring.getCategory().getName() + "行为";
                                String WarringMsg = "[山河智能] -人员安全报警\n" + "尊敬的用户:您好，根据数据监控显示，桩工车间" + eventwarring.getRoad() + "有人员" + eventwarring.getCategory().getName() +
                                        "进入车间，为了安全保障，请管理人员尽快去车间现场核实情况。点击链接查看详情: http://58.20.21.223:9401" + eventwarring.getCover();
                                System.out.println(WarringMsg);
//                                eventService.sendMsgWarring(WarringMsg);   //微信报警调用
                                WebSocketServer.sendInfo(road + "检测到" + category, "222");     //发送警告信息
                            } else {
                                eventService.updateIsWarning("0", eventid);
                            }
                        }
                        System.out.println(id + "查询最新id++++++++++++++++++++");

                }
                redisService.set(key, id); //更新redis中id的值
            }
    }
}
