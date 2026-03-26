package com.bookstore.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Category {
    private Integer id;
    private String name;
    private String description;
    private Integer parent_id;
    private Integer sort_order;
    private int is_active;
    private LocalDateTime created_time;

    public boolean isRootCategory() {
        return parent_id==null||parent_id==0;
    }
    private List<Category>children;
    private Integer bookCount;//该分类下图书数量

    public int getParentId() {
        return parent_id;
    }
    public void setParent(Category parent) {
    }

    public void setSortOrder(int i) {
    }

    public void setStatus(int i) {
    }

    public void setCreateTime(LocalDateTime now) {
    }

    public Integer getSortOrder() {return sort_order;}

    public void setParentId(Integer newParent_id) {
    }

    public Integer getStatus() {return is_active;}
}
