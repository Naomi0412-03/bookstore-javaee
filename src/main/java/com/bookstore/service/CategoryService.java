package com.bookstore.service;

import com.bookstore.entity.Category;
import java.util.List;
import java.util.Map;

public interface CategoryService {

    // 获取所有分类（树形结构）
    List<Category> getCategoryTree();

    // 获取所有顶级分类
    List<Category> getRootCategories();

    // 根据父ID获取子分类
    List<Category> getChildrenByParentId(Integer parentId);

    // 根据ID获取分类详情
    Category getCategoryById(Integer id);

    // 创建分类
    Category createCategory(Category category);

    // 更新分类
    Category updateCategory(Category category);

    // 删除分类（逻辑删除）
    boolean deleteCategory(Integer id);

    // 批量删除分类
    boolean batchDeleteCategories(List<Integer> ids);

    // 启用/禁用分类
    boolean toggleCategoryStatus(Integer id, Integer status);

    // 获取分类路径（从根到当前分类）
    List<Category> getCategoryPath(Integer categoryId);

    // 获取分类下的图书数量
    Integer getBookCountByCategoryId(Integer categoryId);

    // 移动分类（修改父分类）
    boolean moveCategory(Integer categoryId, Integer newParentId);

    // 更新分类排序
    boolean updateCategoryOrder(List<Map<String, Integer>> sortList);

    // 验证分类名称是否重复
    boolean isCategoryNameExists(String name, Integer excludeId);

    // 搜索分类
    List<Category> searchCategories(String keyword);

    List<Category> getAllCategory();
}