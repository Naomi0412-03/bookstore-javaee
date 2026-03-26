package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    //显示注册界面
    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try{
            User registeredUser=userService.register(user);
            response.put("success",true);
            response.put("data",registeredUser);
            response.put("message","注册成功");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    //用户登录
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(
            @RequestParam String  username,
            @RequestParam String  password,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try{
            User user = userService.login(username, password);
            session.setAttribute("user",user);
            response.put("success",true);
            response.put("data",user);
            response.put("message","登录成功");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    //获取当前登录用户信息
    @GetMapping("/current")
    public ResponseEntity<Map<String,Object>> getCurrentUser(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        User user = (User)session.getAttribute("user");
        if(user==null){
            response.put("success",false);
            response.put("message","用户未登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        user=userService.getUserById(user.getId());
        response.put("success",true);
        response.put("data",user);
        return ResponseEntity.ok(response);
    }
    //更新用户信息
    @PutMapping("/{id}")
    public ResponseEntity<Map<String,Object>> updateUser(
            @PathVariable Integer id,
            @RequestBody User user,
            HttpSession session){
        Map<String, Object> response = new HashMap<>();
        try {
            //检查权限：只能修改自己的信息
            User currentUser = (User)session.getAttribute("user");
            if(currentUser==null||!currentUser.getId().equals(id)){
                response.put("success",false);
                response.put("message","无权修改");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            user.setId(id);
            User updatedUser=userService.updateUser(user);

            session.setAttribute("user",updatedUser);
            response.put("success",true);
            response.put("data",updatedUser);
            response.put("message","更新成功");
            return ResponseEntity.ok(response);

        }catch (Exception e){
            response.put("success",false);
            response.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    // 检查用户名是否可用
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        Map<String, Object> response = new HashMap<>();

        boolean exist = userService.isUsernameExist(username);
        boolean available=!exist;
        response.put("success", true);
        response.put("available", available);
        response.put("message", available ? "用户名可用" : "用户名已存在");

        return ResponseEntity.ok(response);
    }

    // 检查邮箱是否可用
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        boolean available = userService.isEmailExist(email);
        response.put("success", true);
        response.put("available", available);
        response.put("message", available ? "邮箱可用" : "邮箱已存在");

        return ResponseEntity.ok(response);
    }
}
