package com.gm.warn.controller;

import com.gm.warn.entity.YBPointData;
import com.gm.warn.result.Result;
import com.gm.warn.result.ResultFactory;
import com.gm.warn.service.YBPointDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
/**
 * 请求格式：
 *       /api/···    不会进行登录拦截
 *       /api/admin/···  会进行登录权限判定拦截
 */
@RestController
public class YBPointDataController {

    @Autowired
    private YBPointDataService ybPointDataService;

    //获取仪表识别数据
    @GetMapping("/api/point/getpointdata")
    public Result getPointData()
    {
        return ResultFactory.buildSuccessResult(ybPointDataService.getAllYBdata());
    }
    //添加识别的仪表数据
    @PostMapping("/api/point/addpointdata")
    public Result addPointData(@RequestBody @Valid YBPointData ybPointData)
    {
        ybPointDataService.addYBData(ybPointData);

        return ResultFactory.buildSuccessResult("成功添加仪表数据");
    }
}
