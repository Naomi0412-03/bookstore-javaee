package com.bookstore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookstore.entity.OrderItem;
import com.bookstore.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/order_items")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    // 获取订单项详情
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderItem(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            OrderItem orderItem = orderItemService.getOrderItemById(id);
            if (orderItem == null) {
                response.put("success", false);
                response.put("message", "订单项不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("data", orderItem);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取订单项失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 根据订单ID获取订单项列表
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);

            response.put("success", true);
            response.put("data", orderItems);
            response.put("total", orderItems.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取订单项列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 创建订单项
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrderItem(@RequestBody OrderItem orderItem) {
        Map<String, Object> response = new HashMap<>();

        try {
            OrderItem createdItem = orderItemService.createOrderItem(orderItem);

            response.put("success", true);
            response.put("data", createdItem);
            response.put("message", "创建订单项成功");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建订单项失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 删除订单项
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrderItem(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean success = orderItemService.deleteOrderItem(id);

            if (success) {
                response.put("success", true);
                response.put("message", "删除订单项成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "订单项不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除订单项失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
