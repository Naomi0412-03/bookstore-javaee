package com.bookstore.service;

import com.bookstore.entity.User;

import java.util.List;

public interface UserService {
    //用户注册
    User register(User user);
    //用户登录
    User login(String username, String password);
    //获取所有用户（手动分页）
    List<User> getAllUsersByPage(int page, int size);

    User getUserById(Integer id);

    //根据用户名查询用户
    User getUserByUsername(String username);
    //更新用户信息
    User updateUser(User user);
    //检查用户名是否存在
    boolean isUsernameExist(String username);
    //检查邮箱是否已经存在
    boolean isEmailExist(String email);
}

