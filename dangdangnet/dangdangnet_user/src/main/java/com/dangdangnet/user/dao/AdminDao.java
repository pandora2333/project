package com.dangdangnet.user.dao;

import com.dangdangnet.user.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Admin账户访问DAO层
 * @author pandora2333
 */
public interface AdminDao extends JpaRepository<Admin,String>, JpaSpecificationExecutor<Admin> {
    public  Admin findAdminByAdminname(String adminname);
}
