package com.bookstore.service.impl;

import com.bookstore.dao.BookMapper;
import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    public BookMapper bookMapper;

    //类别
    @Override
    public List<Book> getBookByCategory(String category) {
        return bookMapper.getBookByCategory(category);
    }
    //书名
    @Override
    public List<Book> getBookByTitle(String title) {
        return bookMapper.getBookByTitle(title);
    }
    //ISBN
    @Override
    public List<Book> getBookByISBN(String ISBN){
        return bookMapper.getBookByISBN(ISBN);
    }
    //作者
    @Override
    public List<Book> getBookByAuthor(String author){
        return bookMapper.getBookByAuthor(author);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    @Override
    public Book getBookById(Integer id) {
        Book book = bookMapper.getBookById(id);
        if(book == null){
            throw new RuntimeException("Book not found");
        }
        return book;

    }

    @Override
    public Book saveBook(Book book) {
        if(book.getId() == null){
            bookMapper.insertBook(book);
            return book;
        }else {
            int rows=bookMapper.updateBook(book);
            if(rows == 0){
                throw new RuntimeException("更新失败");
            }
            return bookMapper.getBookById(book.getId());
        }
    }

    @Override
    public void deleteBook(Integer id) {
        bookMapper.deleteBook(id);
    }
}
