package com.bookstore.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private Integer id;
    private Integer user_id;           // 用户ID
    private Integer book_id;           // 图书ID
    private Integer quantity;         // 购买数量
    private LocalDateTime created_time;    // 加入时间
    private Boolean selected;         // 是否选中（用于批量结算）

    // 关联字段（非数据库字段）
    private Book book;
    private BigDecimal subtotal;      // 小计金额

    // 计算小计
    public BigDecimal getSubtotal() {
        if (book != null && book.getPrice() != null && quantity != null) {
            return book.getPrice().multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
}