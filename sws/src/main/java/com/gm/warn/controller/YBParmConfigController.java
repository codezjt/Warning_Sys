package com.gm.warn.controller;

import com.gm.warn.entity.YBParmConfig;
import com.gm.warn.result.Result;
import com.gm.warn.result.ResultFactory;
import com.gm.warn.service.YBParmConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/**
 * 请求格式：
 *       /api/···    不会进行登录拦截
 *       /api/admin/···  会进行登录权限判定拦截
 */
@RestController
public class YBParmConfigController {
    @Autowired
    private YBParmConfigService ybParmConfigService;

    //添加划定的仪表识别范围参数
    @PostMapping("/api/led/addconfig")
    public Result addLedConfig(@RequestBody @Valid YBParmConfig YBParmConfig){
        ybParmConfigService.addYBparmConfig(YBParmConfig);
        return ResultFactory.buildSuccessResult("成功添加仪表参数");
    }
    //获取划定的仪表识别范围参数
    @GetMapping("/api/led/getconfig")
    public Result getLedConfig()
    {
        return ResultFactory.buildSuccessResult(ybParmConfigService.getAllConfig());
    }
}
