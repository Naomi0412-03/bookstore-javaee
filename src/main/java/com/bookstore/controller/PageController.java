package com.bookstore.controller;
import com.bookstore.entity.Book;
import com.bookstore.entity.User;
import com.bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Controller

public class PageController {
    private static final Logger logger = LoggerFactory.getLogger(PageController.class);
    private final BookService bookService;

    public PageController(BookService bookService) {
        this.bookService = bookService;
    }

    // 首页
    @GetMapping("/")
    public String index() {
        logger.info("=== PageController.index() 被调用 ===");
        return "index"; // 对应 templates/index.html
    }

    // 用户注册页面
    @GetMapping("/user/register")
    public String registerPage() {
        return "user/register"; // 对应 templates/user/register.html
    }

    // 用户登录页面
    @GetMapping("/user/login")
    public String loginPage(@RequestParam(value = "error",required = false)String error, Model model) {
        logger.info("访问登录页面");
        if (error != null) {
            model.addAttribute("error","用户名或密码错误");
        }
        return "user/login"; // 对应 templates/user/login.html
    }

    // 图书列表页面
    @GetMapping("/book/list")
    public String bookListPage(HttpSession session, Model model) {
        logger.info("访问图书列表页面");
        Object loggedIn = session.getAttribute("loggedIn");
        if (loggedIn == null||!(Boolean) loggedIn) {
            logger.warn("用户未登录，重定向登录页面");
            return "redirect:/user/login";
        }
        // 获取用户名并传递给模板
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        logger.info("用户 {} 成功访问图书列表", username);
        return "book/list"; // 对应 templates/book/list.html
    }

    // 图书详情页面
    @GetMapping("/book/detail/{id}")
    public String bookDetailPage(@PathVariable Integer id,
                                 HttpSession session,
                                 Model model) {
        logger.info("访问图书详情页面，bookId={}", id);

        // 检查登录状态
        Object loggedIn = session.getAttribute("loggedIn");
        if (loggedIn == null || !(Boolean) loggedIn) {
            return "redirect:/user/login";
    }

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        Book book=bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("bookId", id);
        return "book/detail";
    }

    // 购物车页面
    @GetMapping("/cart")
    public String cartPage() {
        return "cart/index";
    }

    // 订单页面
    @GetMapping("/orders")
    public String ordersPage() {
        return "order/list";
    }
    //结算页面
    @GetMapping("/order/confirm")
    public String orderConfirmPage(HttpSession session, Model model) {
        Object loggedIn = session.getAttribute("loggedIn");
        if (loggedIn == null || !(Boolean) loggedIn) {
            return "redirect:/user/login";

        }
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "order/confirm";
    }

    // 用户中心
    @GetMapping("/user/profile")
    public String profilePage() {
        return "user/profile";
    }
}