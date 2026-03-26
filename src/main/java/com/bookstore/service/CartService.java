package com.bookstore.service;

import com.bookstore.entity.Cart;
import com.bookstore.vo.CartVO;
import java.util.List;

public interface CartService {

    // 获取用户购物车详情（包含图书信息）
    CartVO getCartDetail(Integer userId);

    // 添加到购物车
    Cart addToCart(Integer userId, Integer bookId, Integer quantity);

    // 更新购物车项数量
    boolean updateQuantity(Integer cartId, Integer quantity, Integer userId);

    // 更新选中状态
    boolean updateSelected(Integer cartId, Boolean selected, Integer userId);

    // 批量更新选中状态
    boolean batchUpdateSelected(List<Integer> cartIds, Boolean selected, Integer userId);

    // 从购物车移除
    boolean removeFromCart(Integer cartId, Integer userId);

    boolean batchRemoveFromCart(List<Integer> cartIds, Integer user_id);

    // 清空购物车
    boolean clearCart(Integer userId);

    // 获取购物车数量
    Integer getCartCount(Integer userId);

    // 检查库存（购物车转订单前验证）
    boolean checkStock(Integer userId);

    List<Cart> getSelectedItems(Integer user_id);
}