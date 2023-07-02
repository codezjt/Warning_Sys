package com.gm.warn.controller;

import com.gm.warn.entity.Event;
import com.gm.warn.service.EventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerTest {

    @Autowired
    EventController eventController;

    @Autowired
    EventService eventService;

    @Test
    public void getCameraRoad() {
        System.out.println(eventController.getCameraRoad());
    }

    @Test
    public void getAllEvents(){
        System.out.println(eventController.listEvents());
    }

    @Test
    public void getEventsByTime(){
        System.out.println(eventController.getImagesBytime("2023-02-04"));
    }
    @Test
    public void getSelectEvents()
    {
        System.out.println(eventController.getImagesByRoadandCategary(3, ""));
    }

    @Test
    public void getImagesByRoad(){
        System.out.println(eventController.getImagesByRoad("线路3"));
    }

    @Test
    public void cuclData(){
        System.out.println(eventController.getCuClData());
    }

    @Test
    public void redisTest(){
        List<Event> events = eventService.getAddEvents(388);
        System.out.println(events.size());
        for(Event event : events)
        {
            if(event.getIswarning().length() == 0) {
                System.out.println(event.getIswarning());
                System.out.println("----------------");
            }
            else
                System.out.println("=============");
        }
    }
}