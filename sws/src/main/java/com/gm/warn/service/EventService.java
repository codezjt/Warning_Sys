package com.gm.warn.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.warn.dao.EventDAO;
import com.gm.warn.entity.Event;
import com.gm.warn.entity.Category;
import com.gm.warn.redis.RedisService;
import com.gm.warn.util.CastUtils;
import com.gm.warn.util.Sha256Utils;


//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class EventService {
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisService redisService;
    @Autowired
    @Qualifier("restTemplateMsg")
    private RestTemplate restTemplate;


    public List<Event> list() {
        List<Event> events;
        String key = "eventlist";
        boolean res = redisService.delete(key);
        Object eventCache = redisService.get(key);

        if (eventCache == null) {
//            Sort sort = new Sort(Sort.Direction.DESC, "id");
            events = eventDAO.getAllEvent();
            redisService.set(key, events);
            System.out.println("从数据库中取值");
        } else {
            events = CastUtils.objectConvertToList(eventCache, Event.class);
            System.out.println("从redis取值");
        }
        return events;
    }
    public List<Event> getAllEventIncludeNoWarn()
    {
        return eventDAO.getAllEventIncludeNoWarn();
    }

//    直接用注解实现缓存
//    @Cacheable(value = RedisConfig.REDIS_KEY_DATABASE)
//    public List<Event> list() {
//        List<Event> events;
//        Sort sort = new Sort(Sort.Direction.DESC, "id");
//        events = eventDAO.findAll(sort);
//        return events;
//    }

    public void addOrUpdate(Event event) {
        redisService.delete("eventlist");
        eventDAO.save(event);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete("eventlist");
    }

    public void deleteById(int id) {
        redisService.delete("eventlist");
        eventDAO.deleteById(id);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete("eventlist");
    }

    public List<Event> listByCategory(int cid) {
        Category category = categoryService.get(cid);
        return eventDAO.findAllByCategory(category);
    }

    public List<Event> Search(String keywords) {
        return eventDAO.findAllByNameLikeOrPartiesLike('%' + keywords + '%', '%' + keywords + '%');
    }

    public List<String> getCameraRoad() {

        return eventDAO.findAllDistinctPress();
    }

    public Event getEventById(int id)
    {
        return eventDAO.getEventBYId(id);
    }

    public List<Event> getImagesByRoadandCategary(int category, String road) {
        return eventDAO.findAllByCidAndPressEquals(category, road);
    }

    public List<Event> getImagesByRoad(String road) {
        return eventDAO.getImagesByRoad(road);
    }

    public List<Event> getAddEvents(int currentId){
        return eventDAO.getAddEventslargeCurrentId(currentId);
    }

    public List<Event> getImagesBytimeandCategary(int category, String time) {
        return eventDAO.findAllByCidAndDateEquals(category, time);
    }

    public List<Event> getImagesBytime(String time) {
        return eventDAO.getImagesByDate(time);
    }

    public ArrayList getEveryCuClData(String firstday) {
        return eventDAO.getEveryCuClData(firstday);
    }

    public List<Event> getreadyWarring(String date, String road) {
        return eventDAO.getReadyWarring(date, road);
    }

    public List<Map<String, Integer>> getLocationData() {
        return eventDAO.getLocationData();
    }

    public ArrayList<Integer> getCountNum(){
        Calendar calendar;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当月第一天
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = format.format(calendar.getTime());

        ArrayList<Integer> res = new ArrayList<Integer>();
        Integer totalWarning = eventDAO.getCountTotal(firstday);
        Integer peopleWarning = eventDAO.getCountPeople();
        Integer locationWarning = eventDAO.getCountLocation();
        Integer equipWarring = eventDAO.getYiBiaonum();
        res.add(totalWarning);
        res.add(peopleWarning);
        res.add(locationWarning);
        res.add(equipWarring);
        return res;
    }

    public List<Event> getReadyWarningEvents() {
        return eventDAO.getReadyWarningEvents();
    }

    public List<Event> getNoWarningEvent() {
        return eventDAO.getNoWarningEvent();
    }

    public void updateIsWarning(String state, int id){
        eventDAO.updataIsWarning(state, id);
    }

    public List<Map<String, Integer>> getWeekCaculate() {
        Calendar calendar;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当月第一天
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String daytime = format.format(calendar.getTime());
//        // 获取当月第一天
//        calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
//
//        String daytime = format.format(calendar.getTime());
        System.out.println(daytime + "时间");
        return eventDAO.getWeekCaculate(daytime);
    }

    public List<Map<String, Integer>> getEquipmentData() {
        return eventDAO.getEquipmentData();
    }

//    public static String request(String url, JSONArray jsonArray) {
//        //map转json
//        String json = JSONObject.toJSONString(jsonArray);
//        String returnValue = "这是默认返回值，接口调用失败";
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//        try {
//            //第一步：创建HttpClient对象
//            httpClient = HttpClients.createDefault();
//            //第二步：创建httpPost对象
//            HttpPost httpPost = new HttpPost(url);
//            //第三步：给httpPost设置JSON格式的参数
//            StringEntity requestEntity = new StringEntity(json, "UTF-8");
//            requestEntity.setContentEncoding("UTF-8");
//            httpPost.setHeader("Content-type", "application/json");
//            httpPost.setEntity(requestEntity);
//            //第五步：发送HttpPost请求，获取返回值
//            response = httpClient.execute(httpPost); //调接口获取返回值时，必须用此方法
////            returnValue = httpClient.execute(httpPost, responseHandler); //调接口获取返回值时，必须用此方法
//
//            entity = response.getEntity();
//            returnValue = EntityUtils.toString(entity, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return returnValue;
//    }
//
//    public void sendWarringMsg(String msg)
//    {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        JSONObject object1= new JSONObject(true);
//        String shaSign = Sha256Utils.getSign(object1.toJSONString(),"TD4pZRg3vT7w5PULGcaZCq58dojLNOL8","6uyitrmCG0z8CYljORgZDR9PIhN4FDda");
//        hashMap.put("sign",shaSign);
//        hashMap.put("code", 5);
//        hashMap.put("sendCode", 013026);
//        hashMap.put("message", msg);
//        hashMap.put("showTimes", 0);
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(hashMap);
//        EventService.request("", jsonArray);
//    }

    public void sendMsgWarring(String msg)
    {
        JSONObject object= new JSONObject(true);
        object.put("code", 7);
        object.put("sendCode", "014926");
        object.put("message", msg);
        object.put("showTimes", 1);
        object.put("secretKey", "TD4pZRg3vT7w5PULGcaZCq58dojLNOL8");
        object.put("secretId", "6uyitrmCG0z8CYljORgZDR9PIhN4FDda");
        String shaSign = Sha256Utils.getSign(object.toJSONString(),"TD4pZRg3vT7w5PULGcaZCq58dojLNOL8","6uyitrmCG0z8CYljORgZDR9PIhN4FDda");
        object.put("sign",shaSign);
        System.out.println(object);
        JSONObject body = doPost("http://forklift.sunward.com.cn/forklift/message/push", restTemplate, object);
    }

    /**
     * @description : 发送post请求
     * @author:mingF
     * @date :2022/5/14 16:51
     * @Param: [url :请求地址,restTemplatelist :请求方法,map :请求数据]
     * @return: [com.alibaba.fastjson.JSONObject]
     **/
    public static JSONObject doPost(String url, RestTemplate restTemplate, Map<String,Object> map) {
        try {
            //String url  = HttpUtil.doPost(ConstantsEnum.DISOUE_WEB_IP+ConstantsEnum.TYBigDataChinaMainMap,paramStr);
            HttpHeaders headers = new HttpHeaders();
            //这里设置的是以payLoad方式提交数据，对于Payload方式，提交的内容一定要是String，且Header要设为“application/json”
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            ObjectMapper mapper = new ObjectMapper();
            JSONObject js = new JSONObject();
            if(map != null){
                for (Map.Entry entry : map.entrySet()){
                    js.put((String) entry.getKey(),entry.getValue());
                }
            }
            String value = mapper.writeValueAsString(js);
            HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
            ResponseEntity<String> res = restTemplate.postForEntity(
                    url,
                    requestEntity, String.class);
            String body = res.getBody();
            if (!StringUtils.isEmpty(body)) {
                return JSONObject.parseObject(body);
            }
        } catch (Exception e) {
            e.printStackTrace();
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.fillInStackTrace().printStackTrace(printWriter);
        }
        return null;
    }
}
