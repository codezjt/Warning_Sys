package com.gm.warn.controller;

import com.gm.warn.entity.YiBiaoPath;
import com.gm.warn.entity.templatePath;
import com.gm.warn.result.Result;
import com.gm.warn.result.ResultFactory;
import com.gm.warn.service.YiBioaPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
/**
 * 请求格式：
 *       /api/···    不会进行登录拦截
 *       /api/admin/···  会进行登录权限判定拦截
 */
@RestController
public class YiBiaoPathController {
    @Autowired
    YiBioaPathService yiBioaPathService;

    //返回液晶仪表模板图片
    @PostMapping("/api/led/gettemplateimg/path")
    public Result getTemplateImageFloat(@RequestBody @Valid templatePath path) throws IOException {
        System.out.println(path + "path+++++++++++++++++++++++++++++");
        YiBiaoPath yiBiaoPath = yiBioaPathService.getYiBiaoPathTemplate(path.getPath());
//        File file = new File(yiBiaoPath.getTemplate_path());
//        byte[] imageBytes = Files.readAllBytes(file.toPath());
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//        headers.setContentLength(imageBytes.length);
//        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        return ResultFactory.buildSuccessResult(yiBiaoPath.getTemplate_path());
    }

    @GetMapping("/api/led/getledtemplate/{path}")
    public Result getLedTemplateImageFloat(@PathVariable("path") String path) throws IOException {
        YiBiaoPath yiBiaoPath = yiBioaPathService.getYiBiaoPathTemplate(path);
        File file = new File(yiBiaoPath.getTemplate_path());
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        return ResultFactory.buildSuccessResult(new ResponseEntity<>(imageBytes, headers, HttpStatus.OK));
    }
}
