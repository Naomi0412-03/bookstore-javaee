package com.bookstore.dao;

import com.bookstore.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    // 插入订单方法（修正字段名）
    @Insert("INSERT INTO bookstore.orders(user_id, order_no, total_amount, status, shipping_address, receiver_name, receiver_phone, created_at) " +
            "VALUES(#{user_id}, #{order_no}, #{total_amount}, #{status}, #{shipping_address}, #{receiver_name}, #{receiver_phone}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Orders order);

    // 根据ID查询订单
    @Select("SELECT * FROM bookstore.orders WHERE id = #{id}")
    Orders getOrderById(Integer id);

    // 根据用户ID查询
    @Select("SELECT * FROM bookstore.orders WHERE user_id = #{user_id} ORDER BY created_at DESC")
    List<Orders> getOrdersByUserId(Integer user_id);

    // 根据订单号查询
    @Select("SELECT * FROM bookstore.orders WHERE order_no = #{order_no}")
    Orders getOrderByOrderNo(String order_no);

    // 状态查询
    @Select("SELECT * FROM bookstore.orders WHERE status = #{status}")
    List<Orders> getOrdersByStatus(Integer status);

    // 更新订单状态
    @Update("UPDATE bookstore.orders SET status = #{status} WHERE id = #{id}")
    int updateOrderStatus(@Param("id") Integer id, @Param("status") Integer status);

    // 更新整个订单
    @Update("UPDATE bookstore.orders SET " +
            "total_amount = #{total_amount}, " +
            "status = #{status}, " +
            "shipping_address = #{shipping_address}, " +
            "receiver_name = #{receiver_name}, " +
            "receiver_phone = #{receiver_phone}, " +
            "payment_method = #{payment_method} " +
            "WHERE id = #{id}")
    int updateOrder(Orders order);

    // 查询所有订单
    @Select("SELECT * FROM bookstore.orders ORDER BY created_at DESC")
    List<Orders> getAllOrders();
}