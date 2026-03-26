package com.bookstore.controller;

import com.bookstore.dao.UserMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Handler;
@Controller
@RequestMapping("/user")
public class LoginController {
    private final UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session){

        logger.info("====== 登录请求开始 ======");
        logger.info("用户名: {}", username);
        logger.info("密码长度: {}", password.length());
        //验证
        boolean isValid=validateUser(username,password);
        if(isValid){
            session.setAttribute("username", username);
            session.setAttribute("loggedIn", true);

            //验证session
            logger.info("Session 设置验证:");
            logger.info("  - username: {}", session.getAttribute("username"));
            logger.info("  - loggedIn: {}", session.getAttribute("loggedIn"));
            logger.info("  - Session ID: {}", session.getId());

            logger.info("====== 登录成功，重定向到 /book/list ======");
            return "redirect:/book/list";
        }else {
            logger.info("登录失败");
            return "redirect:/user/login?error=true";
        }
    }

    private boolean validateUser(String username, String password) {
        logger.info("验证用户: {}", username);
        if(userMapper == null){
            logger.error("userMapper is null");
            return false;
        }
        var user = userMapper.findByUsername(username);
        if(user!=null) {
            logger.info("找到用户: {}", user.getUsername());
            String dbPassword = user.getPassword();
            logger.info("数据库中的MD5密码: {}", dbPassword);

            // 对输入密码进行MD5加密
            String md5Password = md5(password);
            logger.info("输入密码的MD5值: {}", md5Password);

            // 比较MD5值
            boolean passwordMatch = dbPassword.equals(md5Password);
            logger.info("MD5密码匹配结果: {}", passwordMatch);

            return passwordMatch;
        } else {
                logger.info("password not match");
                return false;
            }
        }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5加密失败: {}", e.getMessage());
            return input; // 失败时返回原密码（不应该发生）
        }

    }
    private void testMd5() {
        // 测试：123456的MD5应该是 e10adc3949ba59abbe56e057f20f883e
        String test = md5("123456");
        logger.info("测试MD5('123456') = {}", test);
        logger.info("是否匹配: {}", test.equals("e10adc3949ba59abbe56e057f20f883e"));
    }
}
