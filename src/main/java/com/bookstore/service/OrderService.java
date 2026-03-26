package com.bookstore.service;
import java.util.List;
import java.util.Map;
import com.bookstore.entity.Orders;

public interface OrderService {
    //创建订单（从购物车）
    Orders createOrder(Integer user_id, List<Map<String,Object>> createItems,
                       String shipping_address, String receiver_name, String receiver_phone);
    //根据ID获取订单（包含订单项）
    Orders getOrderById(Integer id);
    //根据订单号获取订单
    Orders getOrderByNo(String order_no);
    //获取用户订单列表
    List<Orders> getUserOrders(Integer user_id);
    //获取所有订单
    List<Orders> getAllOrders();
    //支付订单
    boolean payOrder(Integer order_id,String payment_method);
    //完成订单
    boolean finishOrder(Integer order_id);
    //取消订单
    boolean cancelOrder(Integer order_id, Integer user_id);
    //生成订单（工具方法）
    String generateOrderNo();

    Orders createOrderFromCart(Integer userId, String shippingAddress,
                               String receiverName, String receiverPhone);
}