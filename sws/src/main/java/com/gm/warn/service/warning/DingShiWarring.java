package com.gm.warn.service.warning;

import com.gm.warn.entity.Event;
import com.gm.warn.redis.RedisService;
import com.gm.warn.service.EventService;
import com.gm.warn.util.SpringContextUtils;
import lombok.SneakyThrows;

import java.util.List;

//@Service
public class DingShiWarring {

//    @Autowired
    private RedisService redisService;


    EventService eventService;
//    private static int currentId = 60;

    public void scheduled(){
        final long timeInterval = 1000;

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                int currentId  = 0;
                int id = 1 ;
                String key = "currentid";
                redisService = SpringContextUtils.getContext().getBean(RedisService.class);


                eventService = SpringContextUtils.getContext().getBean(EventService.class);
                while (true) {
                    Object redisGetId = redisService.get(key);

                    System.out.println(redisGetId + "初始redisGetId+++++++++++++++++++++++");

                    if(redisGetId == null)             //看redis中是否有存id值
                    {
                        redisService.set(key, 0);
                    }
                    else {
                        currentId = (int)redisGetId;       //redis中有值的话赋值为当前id
                        System.out.println(currentId + "currentId+++++++++++++++++++++++");
                        System.out.println(redisGetId + "redisGetId+++++++++++++++++++++++");
                    }
                    List<Event> events = eventService.getAddEvents(currentId);
                    if (events.isEmpty()){
                        for (Event event : events){
                            String road = event.getRoad();
                            String category = event.getCategory().getName(); //获取类别空指针异常
                            id = event.getId();
                            WebSocketServer.sendInfo(road + "检测到" + category, "222");
                            System.out.println(id + "查询最新id++++++++++++++++++++");
                        }
                        redisService.set(key, id); //更新redis中id的值
                    }
                    try {
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
