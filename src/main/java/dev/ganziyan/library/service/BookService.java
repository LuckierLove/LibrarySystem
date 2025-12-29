package dev.ganziyan.library.service;

import dev.ganziyan.library.dao.BookDao;
import dev.ganziyan.library.entity.Book;

import java.util.List;

/**
 * 图书服务类
 * 实现图书管理的业务逻辑
 *
 */
public class BookService {
    
    private BookDao bookDao = new BookDao();
    
    /**
     * 查询所有图书
     * 
     * @return 图书列表
     */
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }
    
    /**
     * 根据ID查询图书
     * 
     * @param bookId 图书ID
     * @return 图书对象
     */
    public Book getBookById(Integer bookId) {
        return bookDao.findById(bookId);
    }
    
    /**
     * 搜索图书
     * 
     * @param keyword 搜索关键词
     * @return 符合条件的图书列表
     */
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookDao.searchBooks(keyword.trim());
    }
    
    /**
     * 添加图书
     * 
     * @param book 图书对象
     * @return true表示成功，false表示失败
     */
    public boolean addBook(Book book) {
        // 业务验证
        if (book == null || book.getBookName() == null || book.getBookName().trim().isEmpty()) {
            return false;
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            return false;
        }
        return bookDao.add(book);
    }
    
    /**
     * 更新图书信息
     * 
     * @param book 图书对象
     * @return true表示成功，false表示失败
     */
    public boolean updateBook(Book book) {
        if (book == null || book.getBookId() == null) {
            return false;
        }
        return bookDao.update(book);
    }
    
    /**
     * 删除图书
     * 
     * @param bookId 图书ID
     * @return true表示成功，false表示失败
     */
    public boolean deleteBook(Integer bookId) {
        if (bookId == null) {
            return false;
        }
        return bookDao.delete(bookId);
    }
}
