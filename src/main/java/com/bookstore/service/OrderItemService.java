package com.bookstore.service;

import com.bookstore.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemService {

    // 根据ID获取订单项
    OrderItem getOrderItemById(Integer id);

    // 根据订单ID获取订单项列表
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    // 创建订单项
    OrderItem createOrderItem(OrderItem orderItem);

    // 批量创建订单项
    List<OrderItem> batchCreateOrderItems(List<OrderItem> orderItems);

    // 更新订单项
    boolean updateOrderItem(OrderItem orderItem);

    // 删除订单项
    boolean deleteOrderItem(Integer id);

    // 根据订单ID删除所有订单项
    boolean deleteOrderItemsByOrderId(Integer orderId);

    // 计算订单项总金额
    BigDecimal calculateTotalAmount(List<OrderItem> orderItems);
}