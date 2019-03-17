package com.dangdangnet.user.dao;

import com.dangdangnet.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
/**
 * User账户访问DAO层
 * @author pandora2333
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {
    User findUserByUsername(String username);
}
