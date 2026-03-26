package com.bookstore.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Data
public class Book {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String categoryName;
    private String description;
    // 设置封面图片
    @Setter
    @Getter
    private String coverImage;
    private Category categoryObj;

    public boolean isAvailable() {
        return stock!=null&&stock > 0;
    }
    private boolean isOnSale(){
        return price!=null&& price.compareTo(BigDecimal.ZERO)>0;
    }
    public boolean decreaseStock(Integer quantity){
        if(stock>=quantity){
            stock=stock-quantity;
            return true;
        }
        return false;
    }
    public boolean increaseStock(Integer quantity){
        if(stock!=null){
            stock=stock+quantity;
        }
        return false;
    }
    public String getCoverImage() {
        if (coverImage == null || coverImage.trim().isEmpty()) {
            return "/images/default-book-cover.jpg"; // 默认图片路径
        }
        return coverImage;
    }
}
