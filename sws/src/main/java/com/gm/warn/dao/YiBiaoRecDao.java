package com.gm.warn.dao;

import com.gm.warn.entity.YiBiaoRec;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface YiBiaoRecDao extends JpaRepository<YiBiaoRec, Integer>, JpaSpecificationExecutor<YiBiaoRec> {

    @Query(value = "SELECT * FROM tzrqdata WHERE DATE(TIME) = ?1", nativeQuery = true)
    List<YiBiaoRec> getTzrqData(String date);

}
