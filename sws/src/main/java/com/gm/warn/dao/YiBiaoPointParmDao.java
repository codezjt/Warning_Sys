package com.gm.warn.dao;

import com.gm.warn.entity.YiBiaoPointParm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface YiBiaoPointParmDao extends JpaRepository<YiBiaoPointParm, Integer>, JpaSpecificationExecutor<YiBiaoPointParm> {

    @Query(value = "select * from pointer_config where path = ?1", nativeQuery = true)
    public YiBiaoPointParm getYiBiaoPointParm(String path);
}
