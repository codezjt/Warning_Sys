package com.gm.warn.controller;

import com.gm.warn.entity.YiBiaoPointParm;
import com.gm.warn.service.YiBiaoPointParmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
/**
 * 请求格式：
 *       /api/···    不会进行登录拦截
 *       /api/admin/···  会进行登录权限判定拦截
 */
@RestController
public class YiBiaoPointParmController {

    @Autowired
    private YiBiaoPointParmService yiBiaoPointParmService;
    //根据摄像头位置获取仪表模板，用于划定识别范围
    @GetMapping("/api/pointer/gettemplate/{path}")
    public ResponseEntity getPointTemplate(@PathVariable("path") String path) throws IOException {
        YiBiaoPointParm yiBiaoPointParm = yiBiaoPointParmService.getPointTemplate(path);
        File file = new File(yiBiaoPointParm.getTemplate_path());
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
