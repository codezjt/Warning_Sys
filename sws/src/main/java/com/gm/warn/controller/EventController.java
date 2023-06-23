package com.gm.warn.controller;

import com.alibaba.fastjson.JSON;

import com.gm.warn.entity.Event;
import com.gm.warn.result.Result;
import com.gm.warn.result.ResultFactory;
import com.gm.warn.service.EventService;
import com.gm.warn.util.StringClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 请求格式：
 *       /api/···    不会进行登录拦截
 *       /api/admin/···  会进行登录权限判定拦截
 */


@RestController
public class EventController {
    @Autowired
    EventService eventService;

    @Value("${file.path}")
    String folder;

    Result result;

    //获取所有已报警事件 对应于数据库中的event表
    @GetMapping("/api/events")
    public Result listEvents() {
        System.out.println(eventService.list().getClass());
        return ResultFactory.buildSuccessResult(eventService.list());
    }
    //获取所有事件，包括未报警和已报警事件，对应sql中event表中所有数据
    @GetMapping("/api/eventsincludenowarn")
    public Result getAllEventIncludeNoWarn(){
        return ResultFactory.buildSuccessResult(eventService.getAllEventIncludeNoWarn());
    }

    //修改事件信息
    @PostMapping("/api/admin/content/events")
    public Result addOrUpdateEvents(@RequestBody @Valid Event event) {
        eventService.addOrUpdate(event);
        return ResultFactory.buildSuccessResult("修改成功");
    }
    // 根据事件id删除事件
    @PostMapping("/api/admin/content/events/delete")
    public Result deleteEvent(@RequestBody @Valid Event event) {
        eventService.deleteById(event.getId());
        return ResultFactory.buildSuccessResult("删除成功");
    }

//    @GetMapping("/api/search")
//    public Result searchResult(@RequestParam("keywords") String keywords) {
//        if ("".equals(keywords)) {
//            return ResultFactory.buildSuccessResult(eventService.list());
//        } else {
//            return ResultFactory.buildSuccessResult(eventService.Search(keywords));
//        }
//    }
    //根据事件的违规分类查找改类违规所有已报警事件
    @GetMapping("/api/categories/{cid}/events")
    public Result listByCategory(@PathVariable("cid") int cid) {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(eventService.listByCategory(cid));
        } else {
            return ResultFactory.buildSuccessResult(eventService.list());
        }
    }

//    @PostMapping("/api/admin/content/events/covers")
//    public String coversUpload(MultipartFile file) {
////        String folder = "D:/workspace/img";
//        File imageFolder = new File(folder);
//        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
//                .substring(file.getOriginalFilename().length() - 4));
//        if (!f.getParentFile().exists())
//            f.getParentFile().mkdirs();
//        try {
//            file.transferTo(f);
//            String imgURL = "http://localhost:9401/api/file/" + f.getName();
//            return imgURL;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }

    //获取全部监控路线，并添加一个‘全部’
    @RequestMapping("/api/events/roads")
    public Result getCameraRoad(){

        List<String> roads = eventService.getCameraRoad();
        List<StringClass> resRoads = new ArrayList<StringClass>();
        resRoads.add(new StringClass("全部"));
        for(String road: roads){
            StringClass s = new StringClass(road);
            resRoads.add(s);
        }
        System.out.println(resRoads.getClass());
//        JSONArray array = JSONArray.fromObject(resRoads);
        return ResultFactory.buildSuccessResult(JSON.toJSON(resRoads));
    }

    @RequestMapping("/api/events/videoroads")
    public Result getCammeraVideoRoad(){
        List<StringClass> resRoads = new ArrayList<StringClass>();
        resRoads.add(new StringClass("../../../static/video/20220526_183147.mp4"));

        return ResultFactory.buildSuccessResult(JSON.toJSON(resRoads));
    }

    //根据行为分类、摄像头位置获取已报警违规事件
    @GetMapping("/api/{category}/{road}/events")
    public Result getImagesByRoadandCategary(@PathVariable("category") int category, @PathVariable("road") String road){
        return ResultFactory.buildSuccessResult(eventService.getImagesByRoadandCategary(category, road));
    }
    //根据摄像头位置获取已报警违规事件
    @GetMapping("api/{road}/events")
    public Result getImagesByRoad(@PathVariable("road") String road){
        return ResultFactory.buildSuccessResult(eventService.getImagesByRoad(road));
    }

    //根据时间、摄像头位置获取已报警事件
    @GetMapping("api/{dateselect}/{selectroad}/warring/events")
    public Result getWarringEvents(@PathVariable("dateselect") String date, @PathVariable("selectroad") String road){
        return ResultFactory.buildSuccessResult(eventService.getreadyWarring(date, road));
    }

    //获取所有已报警事件
    @GetMapping("api/readywarning/events")
    public Result getReadyWarningEvent(){
        return ResultFactory.buildSuccessResult(eventService.getReadyWarningEvents());
    }

    //获取所有未报警事件
    @GetMapping("api/nowarning/events")
    public Result getNoWarningEvent(){
        return ResultFactory.buildSuccessResult(eventService.getNoWarningEvent());
    }

    //根据违规行为分类、时间获取所有已报警事件
    @GetMapping("/api/{category}/{time}/time/events")
    public Result getImagesBytimeandCategary(@PathVariable("category") int category, @PathVariable("time") String time){
        return ResultFactory.buildSuccessResult(eventService.getImagesBytimeandCategary(category, time));
    }

    //根据时间获取所有已报警事件
    @GetMapping("api/{time}/time/events")
    public Result getImagesBytime(@PathVariable("time") String time){
        return ResultFactory.buildSuccessResult(eventService.getImagesBytime(time));
    }


    @GetMapping("api/month/data")
    public Result getCuClData(){
        Calendar calendar;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当月第一天
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = format.format(calendar.getTime());

        return ResultFactory.buildSuccessResult((eventService.getEveryCuClData(firstday)));
    }

    //获取当月已报警次数
    @GetMapping("api/people/data")
    public Result getpeopleCuClData(){
        Calendar calendar;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return ResultFactory.buildSuccessResult((eventService.getEveryCuClData("1970-01-01")));
    }

    //获取每个位置的摄像头的检测到的违规行为次数
    @GetMapping("api/location/data")
    public Result getLocationData(){
        return ResultFactory.buildSuccessResult(eventService.getLocationData());
    }

    //获取仪表报警次数
    @GetMapping("api/equipment/data")
    public Result getEquipmentData()
    {
        return ResultFactory.buildSuccessResult(eventService.getEquipmentData());
    }

    //人工审核报警
    @PutMapping("api/admin/content/updateiswarning/{id}")
    public Result upDateIsWarning(@PathVariable("id") int id){

        System.out.println("接收到请求id ----------------------" + id);

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String warringTime = formatter.format(date);
        eventService.updateIsWarning(warringTime, id);
        Event event = eventService.getEventById(id);
//        String WarringMsg = event.getRoad() + "摄像头在" + event.getDate() + "检测到" + event.getCategory().getName() + "行为";
        String WarringMsg = "[山河智能] -人员安全报警\n" +"尊敬的用户:您好，根据数据监控显示，桩工车间A栋2大门有人员" + event.getCategory() + "帽进入车间，为了安全保障，请管理人员尽快去车间现场核实情况。 (" + formatter.format(date) + ")";
//        eventService.sendMsgWarring(WarringMsg);   //报警函数调用
        System.out.println(WarringMsg);
        // 发出警告
        System.out.println("发出警告");
        //发出警告结束
        return ResultFactory.buildSuccessResult("已警告");
    }

    //获取本周不合规行为次数
    @GetMapping("api/admin/getweekcaculate")
    public Result getWeekCaculate()
    {
        return ResultFactory.buildSuccessResult(eventService.getWeekCaculate());
    }

    //获取仪表模板图片，用户划定识别范围
    @GetMapping("/api/gettemplate/{ip}")
    public Result getTemplate(@PathVariable("ip") String ip)
    {

        if(ip.equals("yb"))
        {
            result = ResultFactory.buildSuccessResult(JSON.toJSON("/file/yb.jpg"));
        }
        else if(ip.equals("zz"))
            result = ResultFactory.buildSuccessResult(JSON.toJSON("/file/zz.png"));

        return result;
    }

//    public ResponseEntity<byte[]> getImage(@PathVariable String source) throws IOException
//    {
//
//    }

}
