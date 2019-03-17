package com.dangdangnet.user.service;

import com.dangdangnet.user.entity.User;

/**
 * 账户service层
 * @author pandora2333
 */
public interface UserService {
    public User login(User user);

    public void save(User user);

    public User findById(String userid);

    public void updateByFrozeen(User user);

    public User findByUsername(String username);
}
