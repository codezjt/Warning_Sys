package com.gm.warn.service;

import com.gm.warn.dao.YiBiaoPathDao;
import com.gm.warn.entity.YiBiaoPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YiBioaPathService {

    @Autowired
    YiBiaoPathDao yiBiaoPathDao;

    public YiBiaoPath getYiBiaoPathTemplate(String path)
    {
        return yiBiaoPathDao.getTemplatePath(path);
    }


}
