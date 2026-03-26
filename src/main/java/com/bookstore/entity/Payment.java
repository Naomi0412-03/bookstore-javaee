package com.bookstore.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Payment {
    private Integer id;
    private String paymentNo;      // 支付流水号
    private Integer orderId;       // 订单ID
    private BigDecimal amount;     // 支付金额
    private String paymentMethod;  // 支付方式：alipay, wechat, bank
    private Integer status;        // 0:待支付 1:支付成功 2:支付失败 3:已退款
    private LocalDateTime payTime; // 支付时间
    private String transactionId;  // 第三方交易号
}