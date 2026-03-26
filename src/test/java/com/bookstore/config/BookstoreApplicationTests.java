package com.bookstore.config;  // ⭐️ 重要：与主启动类同一个包

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookServiceTest{
    @Autowired
    private BookService bookService;
    @Test
    void testGetAllBooks() {
        List<Book> books = bookService.getAllBooks();
        assertThat(books).isNotNull();
    }
}