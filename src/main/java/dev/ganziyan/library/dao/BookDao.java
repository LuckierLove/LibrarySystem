package dev.ganziyan.library.dao;

import dev.ganziyan.library.entity.Book;
import dev.ganziyan.library.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书数据访问对象
 * 负责图书信息的数据库CRUD操作
 */
public class BookDao {
    
    /**
     * 查询所有图书
     * 
     * @return 图书列表
     */
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT * FROM books ORDER BY book_id DESC";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Book book = extractBookFromResultSet(resultSet);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("查询所有图书失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return books;
    }
    
    /**
     * 根据ID查询图书
     * 
     * @param bookId 图书ID
     * @return 图书对象，如果不存在则返回null
     */
    public Book findById(Integer bookId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT * FROM books WHERE book_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return extractBookFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("根据ID查询图书失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return null;
    }
    
    /**
     * 根据关键词搜索图书（模糊查询书名、作者、出版社）
     * 
     * @param keyword 搜索关键词
     * @return 符合条件的图书列表
     */
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT * FROM books WHERE book_name LIKE ? OR author LIKE ? " +
                        "OR publisher LIKE ? OR category LIKE ? ORDER BY book_id DESC";
            preparedStatement = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);
            preparedStatement.setString(4, searchPattern);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Book book = extractBookFromResultSet(resultSet);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("搜索图书失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return books;
    }
    
    /**
     * 添加图书
     * 
     * @param book 图书对象
     * @return true表示添加成功，false表示添加失败
     */
    public boolean add(Book book) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "INSERT INTO books (book_name, author, publisher, isbn, category, " +
                        "total_quantity, available_quantity, publish_date, price, description) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getBookName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.setString(5, book.getCategory());
            preparedStatement.setInt(6, book.getTotalQuantity());
            preparedStatement.setInt(7, book.getAvailableQuantity());
            preparedStatement.setDate(8, book.getPublishDate());
            preparedStatement.setBigDecimal(9, book.getPrice());
            preparedStatement.setString(10, book.getDescription());
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("添加图书失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 更新图书信息
     * 
     * @param book 图书对象
     * @return true表示更新成功，false表示更新失败
     */
    public boolean update(Book book) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "UPDATE books SET book_name = ?, author = ?, publisher = ?, " +
                        "isbn = ?, category = ?, total_quantity = ?, available_quantity = ?, " +
                        "publish_date = ?, price = ?, description = ? WHERE book_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getBookName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getPublisher());
            preparedStatement.setString(4, book.getIsbn());
            preparedStatement.setString(5, book.getCategory());
            preparedStatement.setInt(6, book.getTotalQuantity());
            preparedStatement.setInt(7, book.getAvailableQuantity());
            preparedStatement.setDate(8, book.getPublishDate());
            preparedStatement.setBigDecimal(9, book.getPrice());
            preparedStatement.setString(10, book.getDescription());
            preparedStatement.setInt(11, book.getBookId());
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("更新图书失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 更新图书可借数量
     * 
     * @param bookId 图书ID
     * @param availableQuantity 新的可借数量
     * @return true表示更新成功，false表示更新失败
     */
    public boolean updateAvailableQuantity(Integer bookId, Integer availableQuantity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "UPDATE books SET available_quantity = ? WHERE book_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, availableQuantity);
            preparedStatement.setInt(2, bookId);
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("更新图书可借数量失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 删除图书
     * 
     * @param bookId 图书ID
     * @return true表示删除成功，false表示删除失败
     */
    public boolean delete(Integer bookId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "DELETE FROM books WHERE book_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("删除图书失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 从ResultSet中提取图书对象
     * 
     * @param resultSet 结果集
     * @return 图书对象
     * @throws SQLException SQL异常
     */
    private Book extractBookFromResultSet(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setBookId(resultSet.getInt("book_id"));
        book.setBookName(resultSet.getString("book_name"));
        book.setAuthor(resultSet.getString("author"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setCategory(resultSet.getString("category"));
        book.setTotalQuantity(resultSet.getInt("total_quantity"));
        book.setAvailableQuantity(resultSet.getInt("available_quantity"));
        book.setPublishDate(resultSet.getDate("publish_date"));
        book.setPrice(resultSet.getBigDecimal("price"));
        book.setDescription(resultSet.getString("description"));
        book.setCreatedAt(resultSet.getTimestamp("created_at"));
        book.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return book;
    }
}
