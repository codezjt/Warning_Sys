package com.gm.warn.dao;

import com.gm.warn.entity.YiBiaoPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface YiBiaoPathDao extends JpaRepository<YiBiaoPath, Integer>, JpaSpecificationExecutor<YiBiaoPath> {

    @Query(value = "SELECT * FROM led_config WHERE PATH = ?1", nativeQuery = true)
    public YiBiaoPath getTemplatePath(String  path);

}
