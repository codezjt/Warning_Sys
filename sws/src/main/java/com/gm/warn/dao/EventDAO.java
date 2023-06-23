package com.gm.warn.dao;

import com.gm.warn.entity.Event;
import com.gm.warn.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface EventDAO extends JpaRepository<Event,Integer>, JpaSpecificationExecutor<Event> {
    List<Event> findAllByCategory(Category category);
    List<Event> findAllByNameLikeOrPartiesLike(String keyword1, String keyword2);

    @Query(value = "select * from event where iswarning != 0 order by id DESC;", nativeQuery = true)
    List<Event> getAllEvent();

    @Query(value = "select * from event order by id DESC;", nativeQuery = true)
    List<Event> getAllEventIncludeNoWarn();

    @Query(value = "select * from event where road = ?2 and cid = ?1  and iswarning != 0", nativeQuery = true)
    List<Event> findAllByCidAndPressEquals(int category, String road);

    @Query(value = "select * from event where id = ?1 and iswarning != 0", nativeQuery = true)
    Event getEventBYId(int id);

    @Query(value = "select distinct road from event ORDER BY road;", nativeQuery = true)
    List<String> findAllDistinctPress();

    @Query(value = "select * from event where road = ?1 and iswarning != 0", nativeQuery = true)
    List<Event> getImagesByRoad(String road);

    @Query(value = "select * from event where id > ?1", nativeQuery = true)
    List<Event> getAddEventslargeCurrentId(int currentId);

    @Query(value = "select * from event where date like %?2% and cid = ?1 and iswarning != 0", nativeQuery = true)
    List<Event> findAllByCidAndDateEquals(int category, String time);

    @Query(value = "select * from event where date like %?1% and iswarning != 0", nativeQuery = true)
    List<Event> getImagesByDate(String time);

    @Query(value = "SELECT COUNT(event.cid) FROM category LEFT JOIN event ON(event.cid = category.id and event.date > ?1 and iswarning != 0) GROUP BY category.id", nativeQuery = true)
    ArrayList getEveryCuClData(String firstday);

    @Query(value = "select * from event where date like %?1% and road like %?2% and  iswarning != 0", nativeQuery = true)
    List<Event> getReadyWarring(String date, String road);

    @Query(value = "SELECT road, COUNT((event.road)) as num FROM event where iswarning != 0 GROUP BY event.road ORDER BY road", nativeQuery = true)
    List<Map<String, Integer>> getLocationData();

    @Query(value = "SELECT COUNT(event.id) FROM event WHERE event.date > ?1  and iswarning != 0", nativeQuery = true)
    Integer getCountTotal(String date);

    @Query(value = "select COUNT(id) from event where name like '%人员不合规%' and iswarning != 0;", nativeQuery = true)
    Integer getCountPeople();

    @Query(value = "select count(id) from event where iswarning != 0", nativeQuery = true)
    Integer getCountLocation();

    @Query(value = "select * from event where iswarning != '0' order by date desc", nativeQuery = true)
    List<Event> getReadyWarningEvents();

    @Query(value = "select * from event where iswarning = '0' order by date desc", nativeQuery = true)
    List<Event> getNoWarningEvent();

    @Transactional
    @Modifying
    @Query(value = "UPDATE event set iswarning = ?1 where id = ?2", nativeQuery = true)
    void updataIsWarning(String state, int id);

//    @Query("select data, count (event)")
    @Query(value = "SELECT SUBSTRING_INDEX(date, ' ', 1) as datet, COUNT(SUBSTRING_INDEX(event.date, ' ', 1)) AS num FROM event where iswarning != 0 GROUP BY datet HAVING datet > ?1", nativeQuery = true)
    List<Map<String, Integer>> getWeekCaculate(String daytime);

    @Query(value = "select count(id) from event where name like '%仪表%' and iswarning != 0", nativeQuery = true)
    Integer getYiBiaonum();

    @Query(value = "SELECT road, COUNT((event.road)) AS num FROM event WHERE cid = 9 and iswarning != 0 GROUP BY event.road", nativeQuery = true)
    List<Map<String, Integer>> getEquipmentData();


}
