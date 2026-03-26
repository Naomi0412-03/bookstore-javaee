package com.bookstore.service.impl;

import com.bookstore.entity.User;
import com.bookstore.dao.UserMapper;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User register(User user){

        if(userMapper.findByUsername(user.getUsername())!=null&&
            userMapper.findByUsername(user.getUsername())!=null){
            throw new RuntimeException("用户名已存在");
        }
        if(user.getEmail()!=null&&
            userMapper.findByEmail(user.getEmail())!=null){
            throw new RuntimeException("邮箱已存在");
        }
        //密码加密
        String encryptedPassword= DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(encryptedPassword);

        //设置创建时间
        user.setCreatedAt(new Date());

        //保存用户
        userMapper.insert(user);
        return user;
    }
    @Override
    public User login(String username, String password) {
        //验证用户名
        User user = userMapper.findByUsername(username);
        if(user==null){
            throw new RuntimeException("用户名或密码错误");
        }
        String encryptedPassword= DigestUtils.md5DigestAsHex(password.getBytes());
        if(!user.getPassword().equals(encryptedPassword)){
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }


    @Override
    public List<User> getAllUsersByPage(int page, int size) {
        List<User> allUsers = userMapper.findAll();
        int start = (page - 1) * size;
        int end = Math.min(start + size, allUsers.size());

        if (start >= allUsers.size()) {
            return new ArrayList<>();
        }

        return allUsers.subList(start, end);
    }

    @Override
    public User getUserById(Integer id) {
        User user= userMapper.findById(id);
        if (user!=null){
            user.setPassword(null);//清除密码
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    @Override
    public User updateUser(User user) {
        int rows=userMapper.update(user);
        if(rows==0){
            throw new RuntimeException("更新失败");
        }
        return userMapper.findById(user.getId());
    }
    @Override
    public boolean isUsernameExist(String username) {
        System.out.println("查询用户名是否存在: " + username);

        User user = userMapper.findByUsername(username);

        // 调试：打印查询结果
        System.out.println("查询结果: " + (user != null ? "存在" : "不存在"));
        if (user != null) {
            System.out.println("找到用户: " + user.getUsername());
        }

        // 如果user不为null，表示用户名已存在
        return user != null;
    }
    @Override
    public boolean isEmailExist(String email) {
        return userMapper.findByEmail(email) != null;
    }
}
