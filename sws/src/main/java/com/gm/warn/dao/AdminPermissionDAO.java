package com.gm.warn.dao;

import com.gm.warn.entity.AdminPermission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminPermissionDAO extends JpaRepository<AdminPermission, Integer> {
    AdminPermission findById(int id);
}
