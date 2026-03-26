package com.bookstore.service.impl;

import com.bookstore.dao.BookMapper;
import com.bookstore.dao.CartMapper;
import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.service.CartService;
import com.bookstore.vo.CartItemVO;
import com.bookstore.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private BookMapper bookMapper;


    @Override
    public CartVO getCartDetail(Integer user_id) {
        // 1. 查询用户的购物车项
        List<Cart> cartItems = cartMapper.findByUserId(user_id);

        // 2. 转换为展示对象
        List<CartItemVO> cartItemVOs = new ArrayList<>();

        for (Cart cart : cartItems) {
            // 查询图书信息
            Book book = bookMapper.getBookById(cart.getBook_id());
            if (book == null) {
                continue; // 图书已下架，跳过
            }

            // 创建展示对象
            CartItemVO vo = new CartItemVO(cart,book);
            vo.setCartId(cart.getId());
            vo.setBookId(cart.getBook_id());
            vo.setBookTitle(book.getTitle());
            vo.setBookAuthor(book.getAuthor());
            vo.setBookPrice(book.getPrice());
            vo.setBookCover(book.getCoverImage());
            vo.setStock(book.getStock());
            vo.setQuantity(cart.getQuantity());
            vo.setSelected(cart.getSelected() != null ? cart.getSelected() : false);
            vo.setSubtotal(book.getPrice().multiply(new BigDecimal(cart.getQuantity())));

            cartItemVOs.add(vo);
        }

        // 3. 返回购物车展示对象
        return new CartVO(cartItemVOs);
    }

    @Override
    public Cart addToCart(Integer user_id, Integer book_id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("购买数量必须大于0");
        }

        // 1. 检查图书是否存在和库存
        Book book = bookMapper.getBookById(book_id);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }

        if (book.getStock() < quantity) {
            throw new RuntimeException("库存不足: " + book.getTitle() + "，库存: " + book.getStock());
        }

        // 2. 检查是否已在购物车
        Cart existingCart = cartMapper.findByUserIdAndBookId(user_id, book_id);

        if (existingCart != null) {
            // 已存在，更新数量
            int newQuantity = existingCart.getQuantity() + quantity;
            cartMapper.updateQuantity(existingCart.getId(), newQuantity);
            existingCart.setQuantity(newQuantity);
            return existingCart;
        } else {
            // 不存在，新增
            Cart cart = new Cart();
            cart.setUser_id(user_id);
            cart.setBook_id(book_id);
            cart.setQuantity(quantity);
            cart.setSelected(true); // 默认选中
            cart.setBook(book); // 设置关联图书

            cartMapper.insert(cart);
            return cart;
        }
    }

    @Override
    public boolean updateQuantity(Integer cartId, Integer quantity, Integer user_id) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("购买数量必须大于0");
        }

        // 1. 验证购物车项存在且属于该用户
        Cart cart = cartMapper.findById(cartId);
        if (cart == null || !cart.getUser_id().equals(user_id)) {
            throw new RuntimeException("购物车项不存在或无权操作");
        }

        // 2. 检查库存
        Book book = bookMapper.getBookById(cart.getBook_id());
        if (book == null) {
            throw new RuntimeException("图书已下架");
        }

        if (book.getStock() < quantity) {
            throw new RuntimeException("库存不足: " + book.getTitle() + "，库存: " + book.getStock());
        }

        // 3. 更新数量
        return cartMapper.updateQuantity(cartId, quantity) > 0;
    }

    @Override
    public boolean updateSelected(Integer cartId, Boolean selected, Integer user_id) {
        Cart cart = cartMapper.findById(cartId);
        if (cart == null || !cart.getUser_id().equals(user_id)) {
            throw new RuntimeException("购物车项不存在或无权操作");
        }

        return cartMapper.updateSelected(cartId, selected) > 0;
    }

    @Override
    public boolean batchUpdateSelected(List<Integer> cartIds, Boolean selected, Integer user_id) {
        if (cartIds == null || cartIds.isEmpty()) {
            return true;
        }

        // 验证所有购物车项都属于该用户
        for (Integer cartId : cartIds) {
            Cart cart = cartMapper.findById(cartId);
            if (cart == null || !cart.getUser_id().equals(user_id)) {
                throw new RuntimeException("存在无效的购物车项");
            }
        }

        return cartMapper.batchUpdateSelected(cartIds, selected) > 0;
    }

    @Override
    public boolean removeFromCart(Integer cartId, Integer userId) {
        Cart cart = cartMapper.findById(cartId);
        if (cart == null || !cart.getUser_id().equals(userId)) {
            throw new RuntimeException("购物车项不存在或无权操作");
        }

        return cartMapper.deleteById(cartId) > 0;
    }

    @Override
    public boolean batchRemoveFromCart(List<Integer> cartIds, Integer user_id) {
        if (cartIds == null || cartIds.isEmpty()) {
            return true;
        }

        // 验证所有权
        for (Integer cartId : cartIds) {
            Cart cart = cartMapper.findById(cartId);
            if (cart == null || !cart.getUser_id().equals(user_id)) {
                throw new RuntimeException("存在无效的购物车项");
            }
        }

        return cartMapper.batchDelete(cartIds) > 0;
    }

    @Override
    public boolean clearCart(Integer userId) {
        return cartMapper.clearByUserId(userId) > 0;
    }

    @Override
    public Integer getCartCount(Integer userId) {
        Integer count = cartMapper.countByUserId(userId);
        return count != null ? count : 0;
    }

    @Override
    public boolean checkStock(Integer userId) {
        List<Cart> cartItems = cartMapper.findByUserId(userId);

        for (Cart cart : cartItems) {
            if (cart.getSelected() != null && cart.getSelected()) {
                Book book = bookMapper.getBookById(cart.getBook_id());
                if (book == null || book.getStock() < cart.getQuantity()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public List<Cart> getSelectedItems(Integer user_id) {
        List<Cart> allItems = cartMapper.findByUserId(user_id);

        return allItems.stream()
                .filter(cart -> cart.getSelected() != null && cart.getSelected())
                .collect(Collectors.toList());
    }
}