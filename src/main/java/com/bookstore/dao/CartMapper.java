package com.bookstore.dao;

import com.bookstore.entity.Cart;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import java.util.List;

@Mapper
public interface CartMapper {

    // 根据用户ID查询购物车
    @Select("SELECT * FROM cart_item WHERE user_id = #{user_id} ORDER BY created_time DESC")
    List<Cart> findByUserId(Integer user_id);

    // 根据ID查询购物车项
    @Select("SELECT * FROM cart_item WHERE id = #{id}")
    Cart findById(Integer id);

    // 查询用户某本书是否已在购物车
    @Select("SELECT * FROM cart_item WHERE user_id = #{user_id} AND book_id = #{book_id}")
    Cart findByUserIdAndBookId(@Param("user_id") Integer user_id,
                               @Param("book_id") Integer book_id);

    // 添加到购物车
    @Insert("INSERT INTO bookstore.cart_item(user_id, book_id, quantity, selected) " +
            "VALUES(#{userId}, #{bookId}, #{quantity}, #{selected})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Cart cart);

    // 更新购物车项数量
    @Update("UPDATE bookstore.cart_item SET quantity = #{quantity} WHERE id = #{id}")
    int updateQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);

    // 更新选中状态
    @Update("UPDATE bookstore.cart_item SET selected = #{selected} WHERE id = #{id}")
    int updateSelected(@Param("id") Integer id, @Param("selected") Boolean selected);

    // 批量更新选中状态
    @UpdateProvider(type = CartSqlProvider.class, method = "batchUpdateSelected")
    int batchUpdateSelected(@Param("ids") List<Integer> ids, @Param("selected") Boolean selected);

    // 删除购物车项
    @Delete("DELETE FROM bookstore.cart_item WHERE id = #{id}")
    int deleteById(Integer id);

    // 批量删除购物车项
    @DeleteProvider(type = CartSqlProvider.class, method = "batchDelete")
    int batchDelete(@Param("ids") List<Integer> ids);

    // 清空用户购物车
    @Delete("DELETE FROM bookstore.cart_item WHERE user_id = #{user_id}")
    int clearByUserId(Integer user_id);

    // 删除选中的购物车项
    @Delete("DELETE FROM bookstore.cart_item WHERE user_id = #{user_id} AND selected = true")
    int deleteSelected(Integer user_id);

    // 统计用户购物车数量
    @Select("SELECT SUM(quantity) FROM cart WHERE user_id = #{user_id}")
    Integer countByUserId(Integer user_id);
}