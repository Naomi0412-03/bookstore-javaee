package com.bookstore.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Address {
    private Integer id;
    private Integer userId;        // 用户ID
    private String receiverName;   // 收货人
    private String receiverPhone;  // 收货电话
    private String province;       // 省
    private String city;           // 市
    private String district;       // 区
    private String detailAddress;  // 详细地址
    private Boolean isDefault;     // 是否默认地址
    private Integer status;        // 状态 0:删除 1:正常
    private LocalDateTime createTime;

}
