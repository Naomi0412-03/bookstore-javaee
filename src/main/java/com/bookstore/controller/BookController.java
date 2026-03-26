package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    //统一返回格式
    private Map<String,Object> createResponse(boolean success,Object data,String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success",success);
        response.put("data",data);
        response.put("message",message);
        response.put("timestamp",System.currentTimeMillis());
        return response;
    }

    //获取所有图书列表
    @GetMapping("/list")
    public ResponseEntity<Map<String,Object>> getAllBooks() {
        try{
            logger.info("调用/api/book/list API");
            List<Book> books;
            if(bookService!=null){
                try{
                    books = bookService.getAllBooks();
                }catch (Exception e){
                    logger.warn("bookService.getAllBooks()失败，使用示例数据:{}",e.getMessage());
                    books=createSampleBooks();
                }
            }else {
                logger.warn("bookService was null");
                books=createSampleBooks();
            }
            logger.warn("返回{}本图书数据",books.size());
            return ResponseEntity.ok(createResponse(true,books,"获取成功"));
        }catch (Exception e) {
            logger.error("获取失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(false,null,"服务器错误:"+e.getMessage()));
        }
    }

    //创建示例图书数据
    private List<Book> createSampleBooks() {
        List<Book> books = new ArrayList<>();

        //一些封面图片
        String[] coverImages={
                "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?ixlib=rb-1.2.1&auto=format&fit=crop&w=400&q=80", // 图书1
                "https://images.unsplash.com/photo-1512820790803-83ca734da794?ixlib=rb-1.2.1&auto=format&fit=crop&w=400&q=80", // 图书2
                "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?ixlib=rb-1.2.1&auto=format&fit=crop&w-400&q=80", // 图书3
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-1.2.1&auto=format&fit=crop&w=400&q=80", // 图书4
                "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?ixlib=rb-1.2.1&auto=format&fit=crop&w=400&q=80", // 图书5
                "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixlib=rb-1.2.1&auto=format&fit=crop&w=400&q=80"  // 图书6
        };
        books.add(createBook(1, "Java编程思想（第4版）", "Bruce Eckel", BigDecimal.valueOf(108.00), "编程语言", 50, coverImages[0]));
        books.add(createBook(2, "Spring Boot实战", "Craig Walls", BigDecimal.valueOf(89.00), "编程语言", 30, coverImages[1]));
        books.add(createBook(3, "Effective Java中文版", "Joshua Bloch", BigDecimal.valueOf(79.00), "编程语言", 25, coverImages[2]));
        books.add(createBook(4, "Python编程：从入门到实践", "Eric Matthes", BigDecimal.valueOf(75.00), "编程语言", 45, coverImages[3]));
        books.add(createBook(5, "深入理解计算机系统", "Randal E.Bryant", BigDecimal.valueOf(99.00), "计算机科学", 20, coverImages[4]));
        books.add(createBook(6, "算法导论（原书第3版）", "Thomas H.Cormen", BigDecimal.valueOf(128.00), "计算机科学", 15, coverImages[5]));
        books.add(createBook(7, "数据结构与算法分析", "Mark Allen Weiss", BigDecimal.valueOf(85.00), "计算机科学", 35, coverImages[0]));
        books.add(createBook(8, "MySQL必知必会", "Ben Forta", BigDecimal.valueOf(49.00), "数据库", 60, coverImages[1]));

        return books;
    }
    private Book createBook(Integer id, String title, String author, BigDecimal price, String category, Integer stock, String coverImage) {
        Book book=new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setCategory(category);
        book.setStock(stock);
        book.setCoverImage(coverImage);
        return book;
    }

    //获取图书详情
    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String,Object>> getBookById(@PathVariable Integer id) {
        try{
            Book book = bookService.getBookById(id);
            if(book != null) {
                return ResponseEntity.ok(createResponse(true,book,"详情获取成功"));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(false,null,"图书不存在"));
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(false,null,"服务器错误"));
        }
    }

    //搜索图书
    @GetMapping("/search")
    public ResponseEntity<Map<String,Object>> searchBooks(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "category",required = false) String category,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "size",defaultValue = "10")int size){
        try{
            List<Book> searchResults;

            if(title != null&&!title.trim().isEmpty()) {
                searchResults=bookService.getBookByTitle(title);
            } else if (category!=null && !category.trim().isEmpty()) {
                searchResults=bookService.getBookByCategory(category);

            }else {
                searchResults=bookService.getAllBooks();
            }

            //实现分页
            int start = (page-1)*size;
            int end = Math.min(start + size, searchResults.size());
            List<Book> pagedResults = searchResults.subList(start, end);
            Map<String,Object> result = new HashMap<>();
            result.put("books",pagedResults);
            result.put("total",searchResults.size());
            result.put("page",page);
            result.put("size",size);
            result.put("totalPages",(int)Math.ceil((double)searchResults.size() / page));
            return ResponseEntity.ok(createResponse(true,result,"搜索成功"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(false,null,"搜索失败"));
        }
    }

    //新增图书
    @PostMapping
    public ResponseEntity<Map<String,Object>> createBook(@RequestBody Book book) {
        try{
            Book savedbook=bookService.saveBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(true, savedbook, "添加成功"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(false,null,"添加失败"));
        }
    }


    //更新图书信息
    @PostMapping("/{id}")
    public ResponseEntity<Map<String,Object>> updateBook(
            @PathVariable Integer id,
            @RequestBody Book book
    ) {
        try{
            Book existingBook=bookService.getBookById(id);
            if(existingBook == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(false,null,"图书不存在"));
            }
            book.setId(id);
            Book updatedBook =bookService.saveBook(book);
            return ResponseEntity.ok(createResponse(true,updatedBook,"更新完成"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(false,null,"更新失败"));
        }
    }

    //删除图书
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> deleteBook(@PathVariable Integer id) {
        try{
            Book book=bookService.getBookById(id);
            if(book == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(false,null,"图书不存在"));
            }
            bookService.deleteBook(id);
            return ResponseEntity.ok(createResponse(true,null,"删除成功"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(false,null,"删除失败"));
        }
    }
}




