package com.gm.warn.controller;

import com.gm.warn.result.Result;
import com.gm.warn.result.ResultFactory;
import com.gm.warn.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 请求格式：
 *       /api/···    不会进行登录拦截
 *       /api/admin/···  会进行登录权限判定拦截
 */

@RestController
public class MenuController {
    @Autowired
    AdminMenuService adminMenuService;

    //获取前端导航栏，用于前端懒加载
    @GetMapping("/api/menu")
    public Result menu() {
        return ResultFactory.buildSuccessResult(adminMenuService.getMenusByCurrentUser());
    }

    //根据用户角色加载不同导航栏，权限不同，可见导航栏不同
    @GetMapping("/api/admin/role/menu")
    public Result listAllMenus() {
        return ResultFactory.buildSuccessResult(adminMenuService.getMenusByRoleId(1));
    }
}
