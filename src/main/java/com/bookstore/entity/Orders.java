package com.bookstore.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Orders {
    private Integer id;
    private Integer user_id;
    private String order_no;
    private BigDecimal total_amount;
    private Integer status; // 0-待付款 1-已付款 2-已发货 3-已完成 4-已取消
    private LocalDateTime created_at;
    private String shipping_address;
    private String receiver_name;
    private String receiver_phone;
    @Getter
    @Setter
    private String payment_method;
    private List<OrderItem> items = new ArrayList<>();

    // 修正setOrderNo方法
    public void setOrderNo(String orderNo) {
        this.order_no = orderNo;
    }

    // 修正getOrderNo方法
    public String getOrderNo() {
        return order_no;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.total_amount = totalAmount;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shipping_address = shippingAddress;
    }

    public void setReceiverName(String receiverName) {
        this.receiver_name = receiverName;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiver_phone = receiverPhone;
    }

    public void setCreateTime(Date date) {
        if (date != null) {
            this.created_at = date.toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
        }
    }

    // 添加getter
    public Integer getUserId() {
        return user_id;
    }

    public BigDecimal getTotalAmount() {
        return total_amount;
    }

    public String getShippingAddress() {
        return shipping_address;
    }

    public String getReceiverName() {
        return receiver_name;
    }

    public String getReceiverPhone() {
        return receiver_phone;
    }

    public boolean canCancel() {
        return status == 0 || status == 1;
    }

    public boolean canPay() {
        return status == 0;
    }

    public boolean isPaid() {
        return status >= 1 && status <= 3;
    }

    public String getStatusText() {
        switch (status) {
            case 0: return "待付款";
            case 1: return "已付款";
            case 2: return "已发货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知";
        }
    }

    public Integer getTotalQuantity() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public void calculateTotalAmount() {
        this.total_amount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}