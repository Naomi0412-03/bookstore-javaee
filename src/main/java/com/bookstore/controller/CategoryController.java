// CategoryController.java
package com.bookstore.controller;

import com.bookstore.entity.Category;
import com.bookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categories = categoryService.getAllCategory();
            response.put("success", true);
            response.put("data", categories);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/tree")
    public ResponseEntity<?> getCategoryTree() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categoryTree = categoryService.getCategoryTree();
            response.put("success", true);
            response.put("data", categoryTree);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}