package com.gm.warn.service;

import com.gm.warn.dao.YiBiaoPointParmDao;
import com.gm.warn.entity.YiBiaoPointParm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YiBiaoPointParmService {

    @Autowired
    private YiBiaoPointParmDao yiBiaoPointParmDao;

    public YiBiaoPointParm getPointTemplate(String path)
    {
        return yiBiaoPointParmDao.getYiBiaoPointParm(path);
    }
}
