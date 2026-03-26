package com.bookstore.vo;

import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import lombok.Data;

import java.math.BigDecimal;

// 购物车项展示类
@Data
public class CartItemVO {
    private String coverImage;
    private Integer cartId;           // 购物车ID
    private Integer bookId;           // 图书ID
    private String bookTitle;         // 图书标题
    private String bookAuthor;        // 作者
    private BigDecimal bookPrice;     // 单价
    private String bookCover;         // 封面图片
    private Integer stock;            // 库存
    private Integer quantity;         // 购买数量
    private BigDecimal subtotal;      // 小计
    private Boolean selected;// 是否选中

    // 构造方法（从Cart和Book构建）
    public CartItemVO(Cart cart, Book book) {
        this.cartId = cart.getId();
        this.bookId = cart.getBook_id();
        this.bookTitle = book.getTitle();
        this.bookAuthor = book.getAuthor();
        this.bookPrice = book.getPrice();
        this.coverImage = book.getCoverImage();  // 调用getCoverImage()
        this.stock = book.getStock();
        this.quantity = cart.getQuantity();
        this.selected = cart.getSelected();
        this.subtotal = book.getPrice().multiply(new BigDecimal(cart.getQuantity()));
    }
}
