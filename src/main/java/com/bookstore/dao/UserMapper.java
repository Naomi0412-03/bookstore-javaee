package com.bookstore.dao;

import com.bookstore.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //插入用户
    @Insert("INSERT INTO bookstore.users(username,password,email,phone,created_at)"+
    "VALUES(#{username},#{password},#{email},#{phone},#{createdAt})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(User user);

    //根据用户ID查询用户
    @Select("SELECT * FROM bookstore.users WHERE id=#{id}")
    User findById(int id);

    //根据用户名查询用户
    @Select("SELECT * FROM bookstore.users WHERE username=#{username}")
    User findByUsername(String username);

    //根据邮箱查询用户
    @Select("SELECT * FROM bookstore.users WHERE email=#{email}")
    User findByEmail(String email);

    //更新用户信息
    @Update("UPDATE bookstore.users SET username=#{username},password=#{password},"+
    "email=#{email},phone=#{phonr} WHERE id=#{id}")
    int update(User user);

    //删除用户
    @Delete("DELETE FROM bookstore.users WHERE id=#{id}")
    int delete(int id);

    //分页查询用户
    @Select("SELECT id, username, email, phone, created_at " +
            "FROM bookstore.users " +
            "ORDER BY created_at DESC")
    List<User> findAll();
}
