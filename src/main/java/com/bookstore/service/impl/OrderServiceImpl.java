package com.bookstore.service.impl;

import com.bookstore.dao.BookMapper;
import com.bookstore.dao.OrderItemMapper;
import com.bookstore.dao.OrderMapper;
import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.Orders;
import com.bookstore.service.CartService;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private CartService cartService;
    @Override
    public Orders createOrder(Integer user_id, List<Map<String, Object>> createItem,
                              String shipping_address, String receiver_name, String receiver_phone) {

        BigDecimal total_price = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Map<String, Object> cart_item : createItem) {
            Integer book_id = (Integer) cart_item.get("book_id");
            Integer quantity = (Integer) cart_item.get("quantity");

            Book book = bookMapper.getBookById(book_id);
            if (book == null) {
                throw new RuntimeException("图书不存在:" + book_id);
            }

            if (book.getStock() < quantity) {
                throw new RuntimeException("库存不足:" + book.getTitle() + ",库存:" + book.getStock());
            }

            BigDecimal price = book.getPrice();
            BigDecimal subtotal = price.multiply(new BigDecimal(quantity));
            total_price = total_price.add(subtotal);

            // 创建订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setBook_id(book_id);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(price);
            orderItem.setSubtotal(subtotal);
            orderItems.add(orderItem);

            // 减少库存
            bookMapper.updateBookStock(book_id, book.getStock() - quantity);
        }

        // 创建订单
        Orders order = new Orders();
        order.setOrderNo(generateOrderNo());  // 使用setOrderNo
        order.setUserId(user_id);
        order.setTotalAmount(total_price);
        order.setStatus(1); // 待付款
        order.setShippingAddress(shipping_address);
        order.setReceiverName(receiver_name);
        order.setReceiverPhone(receiver_phone);
        order.setCreateTime(new Date());

        // 保存订单
        orderMapper.insert(order);

        // 保存订单项（设置订单ID并批量插入）
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());  // 使用setOrderId
        }
        orderItemMapper.batchInsert(orderItems);

        // 设置关联的订单项
        order.setItems(orderItems);

        return order;
    }

    @Override
    public Orders getOrderById(Integer id) {
        Orders order = orderMapper.getOrderById(id);
        if (order != null) {
            // 获取订单项列表（注意：返回的是List<OrderItem>）
            List<OrderItem> items = orderItemMapper.getOrderItemByOrderId(id);

            // 为每个订单项设置图书信息
            for (OrderItem item : items) {
                Book book = bookMapper.getBookById(item.getBook_id());
                item.setBook(book);
            }
            order.setItems(items);
        }
        return order;
    }

    @Override
    public Orders getOrderByNo(String order_no) {
        return orderMapper.getOrderByOrderNo(order_no);
    }

    @Override
    public List<Orders> getUserOrders(Integer user_id) {
        List<Orders> orders = orderMapper.getOrdersByUserId(user_id);
        // 为每个订单加载订单项
        for (Orders order : orders) {
            List<OrderItem> items = orderItemMapper.getOrderItemByOrderId(order.getId());
            order.setItems(items);
        }
        return orders;
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public boolean payOrder(Integer order_id, String payment_method) {
        Orders order = orderMapper.getOrderById(order_id);
        if (order != null && order.getStatus() == 0) {  // 待付款状态
            order.setStatus(1);  // 已付款
            // 需要添加setPayment_method方法到Orders实体类
            // order.setPayment_method(payment_method);
            return orderMapper.updateOrderStatus(order_id, 1) > 0;
        }
        return false;
    }

    @Override
    public boolean finishOrder(Integer order_id) {
        Orders order = orderMapper.getOrderById(order_id);
        if (order != null && order.getStatus() == 2) {  // 已发货状态
            return orderMapper.updateOrderStatus(order_id, 3) > 0;  // 已完成
        }
        return false;
    }

    @Override
    public boolean cancelOrder(Integer order_id, Integer user_id) {
        Orders order = orderMapper.getOrderById(order_id);
        if (order != null && order.getUserId().equals(user_id) && order.getStatus() == 0) {
            return orderMapper.updateOrderStatus(order_id, 4) > 0;  // 已取消
        }
        return false;
    }

    @Override
    public String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        String random = String.valueOf((int) (Math.random() * 1000));
        return timestamp + random;
    }
    @Override
    public Orders createOrderFromCart(Integer userId, String shippingAddress,
                                      String receiverName, String receiverPhone) {

        // 1. 获取选中的购物车项
        List<Cart> cartItems = cartService.getSelectedItems(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("请选择要购买的商品");
        }

        // 2. 转换为订单创建参数
        List<Map<String, Object>> orderItems = new ArrayList<>();
        for (Cart cart : cartItems) {
            Map<String, Object> item = new HashMap<>();
            item.put("book_id", cart.getBook_id());
            item.put("quantity", cart.getQuantity());
            orderItems.add(item);
        }

        // 3. 创建订单
        Orders order = createOrder(userId, orderItems, shippingAddress, receiverName, receiverPhone);

        // 4. 清空选中的购物车项
        cartService.batchRemoveFromCart(
                cartItems.stream().map(Cart::getId).collect(Collectors.toList()),
                userId
        );

        return order;
    }
}