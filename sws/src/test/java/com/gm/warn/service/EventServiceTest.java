package com.gm.warn.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceTest {

    @Autowired
    EventService eventService;

    @Autowired
    YiBiaoRecService yiBiaoRecService;

    @Test
    public void listByCategory() {
        System.out.println(eventService.listByCategory(1));
    }

    @Test
    public void getEventsByTime(){
        System.out.println(eventService.getImagesBytime("2023-02-04"));
    }

    @Test
    public void getAllEvents(){
        System.out.println(eventService.list());
    }

    @Test
    public void getAllRoad() {
        System.out.println(eventService.getCameraRoad());
    }

    @Test
    public void getEventsBylargeId(){
        System.out.println(eventService.getAddEvents(65));
    }

    @Test
    public void getdata(){
//        System.out.println(eventService.getEveryCuClData("2023-03-04"));
        List<Map<String, Integer>> res = eventService.getWeekCaculate();
        System.out.println(res.size());
    }

    @Test
    public void getdata1(){
        System.out.println(eventService.getLocationData());
    }

    @Test
    public void getdata2(){
        System.out.println(eventService.getCountNum());
    }

    @Test
    public void update(){
        eventService.updateIsWarning("1", 113);
    }

    @Test
    public void sendMessage()
    {
        String msg = "湖大-行为识别测试";
        eventService.sendMsgWarring(msg);
    }

    @Test
    public void getTzrqData()
    {
        System.out.println(yiBiaoRecService.getTzrqData("2023-4-26"));
    }

    @Test
    public void TestStringToInt()
    {
        String accuracy = "98%";
        String accstring = accuracy.replace("%", "");
        Double acc = Double.valueOf(accstring.toString());

        if(acc >= 90)
            System.out.println(acc);
        else
            System.out.println("error");
    }

    @Test
    public void LinshiTestTime()
    {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间？？？？？
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);
        System.out.println(time);
    }
}