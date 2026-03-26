package com.bookstore.service;

import com.bookstore.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getBookByCategory(String category);
    List<Book> getBookByTitle(String title);
    List<Book> getBookByISBN(String ISBN);
    List<Book> getBookByAuthor(String author);
    List<Book> getAllBooks();
    Book getBookById(Integer id);
    Book saveBook(Book book);
    void deleteBook(Integer id);
}
