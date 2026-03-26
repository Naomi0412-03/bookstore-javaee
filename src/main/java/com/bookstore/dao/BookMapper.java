package com.bookstore.dao;

import com.bookstore.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {
    //插入新书
    @Insert("INSERT INTO bookstore.books(title,author,isbn,price,stock,category,coverimage)"+
    "VALUES (#{title},#{author},#{isbn},#{price},#{stock},#{category},#{coverimage})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertBook(Book book);

    //查所有
    @Select("SELECT * FROM bookstore.books")
    List<Book> getAllBooks();

    //id查询
    @Select("SELECT * FROM bookstore.books WHERE id=#{id}")
    Book getBookById(Integer id);

    //根据ISBN查询图书
    @Select("SELECT * FROM bookstore.books WHERE ISBN=#{ISBN}")
    List<Book> getBookByISBN(String ISBN);

    //根据图书作者查询图书
    @Select("SELECT * FROM bookstore.books WHERE author=#{author}")
    List<Book> getBookByAuthor(String author);

    //根据书名查书
    @Select("SELECT * FROM bookstore.books WHERE title=#{title}")
    List<Book> getBookByTitle(String title);
    //根据图书类别查询图书
    @Select("SELECT * FROM bookstore.books WHERE category=#{category}")
    List<Book> getBookByCategory(String category);

    //更新图书信息
    @Update("UPDATE bookstore.books SET " +
            "title=#{title},author=#{author},ISBN=#{ISBN}," +
            "price=#{price},stock=#{stock},category=#{category},coverimage=#{coverimage} "+
            "WHERE id=#{id}")
    int updateBook(Book book);

    //删除图书
    @Delete("DELETE FROM bookstore.books WHERE id=#{id}")
    int deleteBook(Integer id);

    //更新库存
    @Update("UPDATE bookstore.books SET stock=#{stock} WHERE id=#{id}")
    void updateBookStock(@Param("bookId") Integer bookId, @Param("stock") Integer stock);

    //统计分类下的图书数量
    @Select("SELECT COUNT(*) FROM bookstore.books WHERE category_id=#{category_id}")
    Integer countByCategoryId(Integer categoryId);
}
