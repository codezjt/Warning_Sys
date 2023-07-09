package com.gm.warn.dao;


import com.gm.warn.entity.CaclData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CaclDao extends JpaRepository<CaclData, Integer> {

    @Transactional
    @Modifying
    @Query(value = "update cacl set cacl = cacl + 1 where name = ?1", nativeQuery = true)
    void updateCacl(String name);

    @Query(value = "insert into cacl(name, cacl) values(?1, ?2)", nativeQuery = true)
    void addCacl(String name, int data);



}
