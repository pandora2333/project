package com.dangdangnet.user.service.impl;

import com.dangdangnet.user.dao.AdminDao;
import com.dangdangnet.user.entity.Admin;
import com.dangdangnet.user.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 管理员service层实现
 * @author pandora2333
 */
@Transactional
@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    AdminDao adminDao;
    @Override
    public Admin login(Admin admin) {
        return adminDao.findAdminByAdminname(admin.getAdminname());
    }
}
