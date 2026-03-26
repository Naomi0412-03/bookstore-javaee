package com.bookstore.service.impl;

import com.bookstore.dao.BookMapper;
import com.bookstore.dao.OrderItemMapper;
import com.bookstore.entity.Book;
import com.bookstore.entity.OrderItem;
import com.bookstore.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private BookMapper bookMapper;

    @Override
    public OrderItem getOrderItemById(Integer id) {
        OrderItem orderItem = orderItemMapper.getOrderItemById(id);
        if (orderItem != null && orderItem.getBook_id() != null) {
            // 加载关联的图书信息
            Book book = bookMapper.getBookById(orderItem.getBook_id());
            orderItem.setBook(book);
        }
        return orderItem;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        List<OrderItem> orderItems = orderItemMapper.getOrderItemByOrderId(orderId);

        // 为每个订单项加载图书信息
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getBook_id() != null) {
                Book book = bookMapper.getBookById(orderItem.getBook_id());
                orderItem.setBook(book);
            }
        }
        return orderItems;
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        // 验证图书是否存在
        if (orderItem.getBook_id() == null) {
            throw new RuntimeException("图书ID不能为空");
        }

        Book book = bookMapper.getBookById(orderItem.getBook_id());
        if (book == null) {
            throw new RuntimeException("图书不存在，ID: " + orderItem.getBook_id());
        }

        // 验证库存
        if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
            throw new RuntimeException("购买数量必须大于0");
        }

        if (book.getStock() < orderItem.getQuantity()) {
            throw new RuntimeException("库存不足: " + book.getTitle() + "，库存: " + book.getStock());
        }

        // 设置价格和计算小计
        if (orderItem.getPrice() == null) {
            orderItem.setPrice(book.getPrice());
        }

        // 计算小计
        orderItem.calculateSubtotal();

        // 设置创建时间
        orderItem.setCreated_time(LocalDateTime.now());

        // 保存订单项
        orderItemMapper.insert(orderItem);

        // 减少库存
        bookMapper.updateBookStock(orderItem.getBook_id(), book.getStock() - orderItem.getQuantity());

        // 设置关联的图书信息
        orderItem.setBook(book);

        return orderItem;
    }

    @Override
    public List<OrderItem> batchCreateOrderItems(List<OrderItem> orderItems) {
        List<OrderItem> savedItems = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            OrderItem savedItem = createOrderItem(orderItem);
            savedItems.add(savedItem);
        }

        return savedItems;
    }

    @Override
    public boolean updateOrderItem(OrderItem orderItem) {
        // 检查订单项是否存在
        OrderItem existingItem = orderItemMapper.getOrderItemById(orderItem.getId());
        if (existingItem == null) {
            throw new RuntimeException("订单项不存在，ID: " + orderItem.getId());
        }

        // 如果修改了图书，需要验证新图书
        if (orderItem.getBook_id() != null && !orderItem.getBook_id().equals(existingItem.getBook_id())) {
            Book book = bookMapper.getBookById(orderItem.getBook_id());
            if (book == null) {
                throw new RuntimeException("图书不存在，ID: " + orderItem.getBook_id());
            }
            orderItem.setPrice(book.getPrice());
        }

        // 如果修改了数量，需要重新计算小计
        if (orderItem.getQuantity() != null) {
            orderItem.calculateSubtotal();
        }

        // 更新订单项
        // 注意：这里需要OrderItemMapper有update方法，如果没有，需要添加
        return orderItemMapper.updateOrderItem(orderItem) > 0;
    }

    @Override
    public boolean deleteOrderItem(Integer id) {
        // 先获取订单项信息，用于恢复库存
        OrderItem orderItem = orderItemMapper.getOrderItemById(id);
        if (orderItem == null) {
            return false;
        }

        // 删除订单项
        orderItemMapper.deleteOrderItem(id);

        // 恢复库存
        if (orderItem.getBook_id() != null && orderItem.getQuantity() != null) {
            Book book = bookMapper.getBookById(orderItem.getBook_id());
            if (book != null) {
                bookMapper.updateBookStock(orderItem.getBook_id(), book.getStock() + orderItem.getQuantity());
            }
        }

        return true;
    }

    @Override
    public boolean deleteOrderItemsByOrderId(Integer orderId) {
        // 获取该订单的所有订单项
        List<OrderItem> orderItems = orderItemMapper.getOrderItemByOrderId(orderId);

        // 批量恢复库存
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getBook_id() != null && orderItem.getQuantity() != null) {
                Book book = bookMapper.getBookById(orderItem.getBook_id());
                if (book != null) {
                    bookMapper.updateBookStock(orderItem.getBook_id(), book.getStock() + orderItem.getQuantity());
                }
            }
        }

        // 批量删除订单项
        orderItemMapper.deleteOrderItemByOrderId(orderId);

        return true;
    }

    @Override
    public BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : orderItems) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
        }

        return total;
    }
}