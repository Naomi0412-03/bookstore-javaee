package com.bookstore.service.impl;

import com.bookstore.dao.CategoryMapper;
import com.bookstore.entity.Category;
import com.bookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategoryTree() {

        List<Category> allCategories = categoryMapper.findAllActive();

        Map<Integer, Category> categoryMap = new HashMap<>();
        List<Category> rootCategories = new ArrayList<>();

        // 放入map
        for (Category category : allCategories) {
            categoryMap.put(category.getId(), category);

            //安全比较
            Integer parentId = category.getParentId();
            if (parentId.equals(0)) {
                rootCategories.add(category);
            }
        }

        // 建立父子关系
        for (Category category : allCategories) {
            Integer parentId = category.getParentId();
            if (!parentId.equals(0)) {
                Category parent = categoryMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(category);
                }
            }
        }

        return rootCategories;
    }

    @Override
    public List<Category> getRootCategories() {
        return categoryMapper.findByParentId(0);
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryMapper.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        // 设置父分类，如果为null则设为0
        if (category.getParent_id() == null) {
            category.setParentId(0);
        }

        category.setCreateTime(LocalDateTime.now());
        if (category.getStatus()== null) {
            category.setStatus(1);
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(99);
        }
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        categoryMapper.update(category);
        return categoryMapper.findById(category.getId());
    }

    @Override
    public boolean deleteCategory(Integer id) {
        return categoryMapper.delete(id) > 0;
    }

    @Override
    public List<Category> getChildrenByParentId(Integer parentId) {
        return categoryMapper.findByParentId(parentId);
    }

    @Override
    public boolean batchDeleteCategories(List<Integer> ids) {
        for (Integer id : ids) {
            deleteCategory(id);
        }
        return true;
    }

    @Override
    public boolean toggleCategoryStatus(Integer id, Integer status) {
        Category category = categoryMapper.findById(id);
        if (category != null) {
            category.setStatus(status);
            return categoryMapper.update(category) > 0;
        }
        return false;
    }

    @Override
    public List<Category> getCategoryPath(Integer categoryId) {
        List<Category> path = new ArrayList<>();
        Category current = categoryMapper.findById(categoryId);

        while (current != null) {
            path.add(0, current); // 添加到开头

            Integer parentId = current.getParentId();
            if (parentId == null || parentId.equals(0)) {
                break;
            }
            current = categoryMapper.findById(parentId);
        }

        return path;
    }

    @Override
    public Integer getBookCountByCategoryId(Integer categoryId) {
        // 这里需要BookMapper有根据分类统计的方法
        // 先返回0
        return 0;
    }

    @Override
    public boolean moveCategory(Integer categoryId, Integer newParentId) {
        Category category = categoryMapper.findById(categoryId);
        if (category != null) {
            // 如果newParentId为null，设为0（根分类）
            category.setParentId(newParentId != null ? newParentId : 0);
            return categoryMapper.update(category) > 0;
        }
        return false;
    }

    @Override
    public boolean updateCategoryOrder(List<Map<String, Integer>> sortList) {
        return true;
    }

    @Override
    public boolean isCategoryNameExists(String name, Integer excludeId) {
        return false;
    }

    @Override
    public List<Category> searchCategories(String keyword) {
        return categoryMapper.findAllActive();
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryMapper.findAllActive();
    }
}