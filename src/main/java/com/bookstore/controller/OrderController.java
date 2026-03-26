package com.bookstore.controller;

import com.bookstore.entity.Orders;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String,Object>> createOrder(
            @RequestBody Map<String,Object> orderRequest,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            //检查登录
            Integer user_id = getUserIdFromSession(session);
            if (user_id == null) {
                return unauthorizedResponse();

            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) orderRequest.get("cartItems");
            String shippingAddress = (String) orderRequest.get("shippingAddress");
            String receiverName = (String) orderRequest.get("receiverName");
            String receiverPhone = (String) orderRequest.get("receiverPhone");

            // 创建订单
            Orders orders = orderService.createOrder(user_id, cartItems, shippingAddress, receiverName, receiverPhone);

            response.put("success", true);
            response.put("data", orders);
            response.put("message", "订单创建成功");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrder(
            @PathVariable Integer id,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                return unauthorizedResponse();
            }

            Orders orders = orderService.getOrderById(id);
            if (orders == null) {
                response.put("success", false);
                response.put("message", "订单不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            // 检查权限
            if (!orders.getUserId().equals(userId)) {
                response.put("success", false);
                response.put("message", "无权查看此订单");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            response.put("success", true);
            response.put("data", orders);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    private Integer getUserIdFromSession(HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> user = (Map<String, Object>) userObj;
            return (Integer) user.get("id");
        }
        return null;
    }

    private ResponseEntity<Map<String, Object>> unauthorizedResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "请先登录");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
