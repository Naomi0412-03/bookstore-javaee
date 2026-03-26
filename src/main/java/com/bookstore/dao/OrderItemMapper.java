package com.bookstore.dao;

import com.bookstore.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    // 根据ID查询订单项
    @Select("SELECT * FROM bookstore.order_item WHERE id = #{id}")
    OrderItem getOrderItemById(Integer id);

    // 根据订单ID查询（应该返回列表）
    @Select("SELECT * FROM bookstore.order_item WHERE order_id = #{order_id}")
    List<OrderItem> getOrderItemByOrderId(Integer order_id);

    // 查询所有订单项
    @Select("SELECT * FROM bookstore.order_item")
    List<OrderItem> getOrderItems();

    // 批量插入订单项
    @Insert("INSERT INTO bookstore.order_item(order_id, book_id, quantity, price, subtotal) "+
            "VALUES (#{item.order_id}, #{item.book_id}, #{item.quantity}, #{item.price}, #{item.subtotal})")
    int batchInsert(@Param("items") List<OrderItem> items);

    // 单个插入
    @Insert("INSERT INTO bookstore.order_item(order_id, book_id, quantity, price, subtotal) " +
            "VALUES(#{order_id}, #{book_id}, #{quantity}, #{price}, #{subtotal})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItem orderItem);

    // 删除订单项（修正SQL）
    @Delete("DELETE FROM bookstore.order_item WHERE book_id = #{book_id}")
    void deleteOrderItem(Integer book_id);

    // 根据订单ID删除
    @Delete("DELETE FROM bookstore.order_item WHERE order_id = #{order_id}")
    void deleteOrderItemByOrderId(Integer order_id);

    @Update("UPDATE bookstore.order_item SET " +
            "order_id = #{order_id}, " +
            "book_id = #{book_id}, " +
            "quantity = #{quantity}, " +
            "price = #{price}, " +
            "subtotal = #{subtotal} " +
            "WHERE id = #{id}")
    int updateOrderItem(OrderItem orderItem);

}