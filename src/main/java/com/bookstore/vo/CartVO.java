package com.bookstore.vo;

import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVO {
    private List<CartItemVO> items;          // 购物车项列表
    private BigDecimal totalAmount;          // 总金额
    private Integer totalQuantity;           // 总数量
    private Integer selectedCount;           // 选中数量
    private BigDecimal selectedAmount;       // 选中商品总金额

    // 构造函数
    public CartVO(List<CartItemVO> items) {
        this.items = items;
        calculateTotals();
    }

    // 计算统计信息
    private void calculateTotals() {
        this.totalQuantity = 0;
        this.totalAmount = BigDecimal.ZERO;
        this.selectedCount = 0;
        this.selectedAmount = BigDecimal.ZERO;

        for (CartItemVO item : items) {
            totalQuantity += item.getQuantity();
            totalAmount = totalAmount.add(item.getSubtotal());

            if (item.getSelected()) {
                selectedCount += item.getQuantity();
                selectedAmount = selectedAmount.add(item.getSubtotal());
            }
        }
    }


}

