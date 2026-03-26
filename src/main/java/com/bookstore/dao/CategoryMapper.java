// CategoryMapper.java
package com.bookstore.dao;

import com.bookstore.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT * FROM categories WHERE status = 1 ORDER BY sort_order ASC")
    List<Category> findAllActive();

    @Select("SELECT * FROM categories WHERE parent_id = #{parentId} AND status = 1 ORDER BY sort_order ASC")
    List<Category> findByParentId(Integer parentId);

    @Select("SELECT * FROM categories WHERE id = #{id}")
    Category findById(Integer id);

    @Insert("INSERT INTO bookstore.categories(name, description, parent_id, sort_order, status) " +
            "VALUES(#{name}, #{description}, #{parentId}, #{sortOrder}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Category category);

    @Update("UPDATE bookstore.categories SET " +
            "name = #{name}, " +
            "description = #{description}, " +
            "parent_id = #{parentId}, " +
            "sort_order = #{sortOrder}, " +
            "status = #{status} " +
            "WHERE id = #{id}")
    int update(Category category);

    @Update("UPDATE bookstore.categories SET status = 0 WHERE id = #{id}")
    int delete(Integer id);
}