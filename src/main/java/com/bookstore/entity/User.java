package com.bookstore.entity;
import lombok.Data;//自动生成getter。setter。toString等标准方法
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Date createdAt;


    //密码验证
    public boolean validatePassword(String inputPassword){
        return this.password!=null&&this.password.equals(inputPassword);
    }

    //邮箱脱敏方法
    public String getMaskEmail(){
        if(email==null) return "";
        int atIndex = email.indexOf("@");
        if (atIndex>3){
            return email.substring(0,2)+"****"+email.substring(atIndex+2);
        }
        return email;
    }
}

