package com.gm.warn.controller;


import com.gm.warn.result.Result;
import com.gm.warn.result.ResultFactory;
import com.gm.warn.service.YiBiaoRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
/**
 * 请求格式：
 *       /api/···    不会进行登录拦截
 *       /api/admin/···  会进行登录权限判定拦截
 */
@RestController
public class YiBiaoRecController {

    @Autowired
    YiBiaoRecService yiBiaoRecService;
    //根据时间获取识别到的仪表数据
    @GetMapping("/api/admin/getTzrqData/{date}")
    public Result getTzrqData(@PathVariable("date") String date)
    {
        return ResultFactory.buildSuccessResult(yiBiaoRecService.getTzrqData(date));
    }
}
