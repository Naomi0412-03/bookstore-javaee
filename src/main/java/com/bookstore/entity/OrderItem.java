package com.bookstore.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderItem {
    private Integer id;
    private Integer order_id;
    private Integer book_id;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private LocalDateTime created_time;
    private Book book;

    // 添加setOrderId方法（因为Service中使用了）
    public void setOrderId(Integer orderId) {
        this.order_id = orderId;
    }

    // 添加getOrderId方法
    public Integer getOrderId() {
        return order_id;
    }

    // 获取订单项小计金额
    public BigDecimal getSubtotal() {
        if (subtotal != null) {
            return subtotal;
        }
        if (price != null && quantity != null) {
            return price.multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }

    // 小计金额并设置小计
    public void calculateSubtotal() {
        if (price != null && quantity != null) {
            this.subtotal = price.multiply(new BigDecimal(quantity));
        }
    }

    // 判断是否同一本书
    public boolean isSameBook(Book book) {
        return this.book_id != null && this.book_id.equals(book.getId());
    }

    // 获取图书信息
    public String getDisplayBookTitle() {
        if (book != null && book.getTitle() != null) {
            return book.getTitle();
        }
        return "未知图书";
    }
}