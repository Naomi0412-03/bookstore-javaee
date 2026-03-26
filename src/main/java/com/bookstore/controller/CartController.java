package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.service.BookService;
import com.bookstore.service.CartService;
import com.bookstore.vo.CartItemVO;
import com.bookstore.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private BookService bookService;

    // 获取购物车详情
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                return unauthorizedResponse();
            }

            CartVO cartDetail = cartService.getCartDetail(userId);

            response.put("success", true);
            response.put("data", cartDetail);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取购物车失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 添加到购物车
    @PostMapping("/add/{bookId}")
    public ResponseEntity<Map<String, Object>> addCart(
            @PathVariable("bookId") Integer bookId,
            @RequestParam(defaultValue = "1") Integer quantity,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try{
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                return unauthorizedResponse();
            }
            Book book = bookService.getBookById(bookId);
            if (book == null) {
                response.put("success", false);
                response.put("message","图书不存在");
                return ResponseEntity.badRequest().body(response);
            }
            if (book.getStock() < quantity) {
                response.put("success", false);
                response.put("message","库存不足");
                return ResponseEntity.badRequest().body(response);
            }
            Cart cartItem=cartService.addToCart(userId,bookId,quantity);
            boolean success=cartItem!=null;
            if (success) {
                Integer cartCount = cartService.getCartCount(userId);
                session.setAttribute("cartCount", cartCount);
                response.put("success", true);
                response.put("message","添加成功");
                response.put("cartCount", cartCount);
                return ResponseEntity.ok(response);
            }else {
                response.put("success", false);
                response.put("message","添加失败");
                return ResponseEntity.badRequest().body(response);
            }
        }catch (Exception e){
            response.put("success", false);
            response.put("message","添加失败"+e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    // 更新数量
    @PutMapping("/{cartId}/quantity")
    public ResponseEntity<Map<String, Object>> updateQuantity(
            @PathVariable Integer cartId,
            @RequestParam Integer quantity,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                return unauthorizedResponse();
            }

            boolean success = cartService.updateQuantity(cartId, quantity, userId);

            response.put("success", success);
            response.put("message", success ? "更新成功" : "更新失败");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 更新选中状态
    @PutMapping("/{cartId}/selected")
    public ResponseEntity<Map<String, Object>> updateSelected(
            @PathVariable Integer cartId,
            @RequestParam Boolean selected,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                return unauthorizedResponse();
            }

            boolean success = cartService.updateSelected(cartId, selected, userId);

            response.put("success", success);
            response.put("message", success ? "更新成功" : "更新失败");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 批量更新选中状态
    @PutMapping("/batch-selected")
    public ResponseEntity<Map<String, Object>> batchUpdateSelected(
            @RequestBody Map<String, Object> request,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                return unauthorizedResponse();
            }

            @SuppressWarnings("unchecked")
            List<Integer> cartIds = (List<Integer>) request.get("cartIds");
            Boolean selected = (Boolean) request.get("selected");

            boolean success = cartService.batchUpdateSelected(cartIds, selected, userId);

            response.put("success", success);
            response.put("message", success ? "批量更新成功" : "批量更新失败");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 移除购物车项
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @PathVariable Integer cartId,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                return unauthorizedResponse();
            }

            boolean success = cartService.removeFromCart(cartId, userId);

            response.put("success", success);
            response.put("message", success ? "移除成功" : "移除失败");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "移除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 批量移除
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchRemove(
            @RequestBody List<Integer> cartIds,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                return unauthorizedResponse();
            }

            boolean success = cartService.batchRemoveFromCart(cartIds, userId);

            response.put("success", success);
            response.put("message", success ? "批量移除成功" : "批量移除失败");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量移除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 获取购物车数量
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCartCount(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            Integer userId = getUserIdFromSession(session);
            if (userId == null) {
                response.put("success", true);
                response.put("data", 0);
                return ResponseEntity.ok(response);
            }

            Integer count = cartService.getCartCount(userId);

            response.put("success", true);
            response.put("data", count);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 工具方法
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