package dev.ganziyan.library.dao;

import dev.ganziyan.library.entity.BorrowRecord;
import dev.ganziyan.library.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 借阅记录数据访问对象
 * 负责借阅记录的数据库CRUD操作
 */
public class BorrowRecordDao {
    
    /**
     * 查询所有借阅记录（包含关联的用户和图书信息）
     * 
     * @return 借阅记录列表
     */
    public List<BorrowRecord> findAll() {
        List<BorrowRecord> records = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT br.*, u.username, u.real_name, b.book_name, b.author " +
                        "FROM borrow_records br " +
                        "JOIN users u ON br.user_id = u.user_id " +
                        "JOIN books b ON br.book_id = b.book_id " +
                        "ORDER BY br.record_id DESC";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                BorrowRecord record = extractBorrowRecordFromResultSet(resultSet);
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("查询所有借阅记录失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return records;
    }
    
    /**
     * 根据用户 ID查询借阅记录
     * 
     * @param userId 用户 ID
     * @return 该用户的借阅记录列表
     */
    public List<BorrowRecord> findByUserId(Integer userId) {
        List<BorrowRecord> records = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT br.*, u.username, u.real_name, b.book_name, b.author " +
                        "FROM borrow_records br " +
                        "JOIN users u ON br.user_id = u.user_id " +
                        "JOIN books b ON br.book_id = b.book_id " +
                        "WHERE br.user_id = ? " +
                        "ORDER BY br.record_id DESC";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                BorrowRecord record = extractBorrowRecordFromResultSet(resultSet);
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("根据用户ID查询借阅记录失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return records;
    }
    
    /**
     * 查询用户当前正在借阅的图书记录
     * 
     * @param userId 用户 ID
     * @return 正在借阅的记录列表
     */
    public List<BorrowRecord> findBorrowingByUserId(Integer userId) {
        List<BorrowRecord> records = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT br.*, u.username, u.real_name, b.book_name, b.author " +
                        "FROM borrow_records br " +
                        "JOIN users u ON br.user_id = u.user_id " +
                        "JOIN books b ON br.book_id = b.book_id " +
                        "WHERE br.user_id = ? AND (br.status = '借阅中' OR br.status = '已逾期') " +
                        "ORDER BY br.record_id DESC";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                BorrowRecord record = extractBorrowRecordFromResultSet(resultSet);
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("查询用户正在借阅的记录失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return records;
    }
    
    /**
     * 根据图书 ID查询借阅记录
     * 
     * @param bookId 图书 ID
     * @return 该图书的借阅记录列表
     */
    public List<BorrowRecord> findByBookId(Integer bookId) {
        List<BorrowRecord> records = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT br.*, u.username, u.real_name, b.book_name, b.author " +
                        "FROM borrow_records br " +
                        "JOIN users u ON br.user_id = u.user_id " +
                        "JOIN books b ON br.book_id = b.book_id " +
                        "WHERE br.book_id = ? " +
                        "ORDER BY br.record_id DESC";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                BorrowRecord record = extractBorrowRecordFromResultSet(resultSet);
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("根据图书ID查询借阅记录失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return records;
    }
    
    /**
     * 添加借阅记录
     * 
     * @param record 借阅记录对象
     * @return true表示添加成功，false表示添加失败
     */
    public boolean add(BorrowRecord record) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "INSERT INTO borrow_records (user_id, book_id, due_date, status, remarks) " +
                        "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, record.getUserId());
            preparedStatement.setInt(2, record.getBookId());
            preparedStatement.setDate(3, record.getDueDate());
            preparedStatement.setString(4, record.getStatus());
            preparedStatement.setString(5, record.getRemarks());
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("添加借阅记录失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 更新借阅记录（归还图书）
     * 
     * @param recordId 记录 ID
     * @return true表示更新成功，false表示更新失败
     */
    public boolean returnBook(Integer recordId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "UPDATE borrow_records SET return_date = NOW(), status = '已归还' " +
                        "WHERE record_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, recordId);
            
            int rows = preparedStatement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("归还图书失败：" + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.closeAll(connection, preparedStatement, null);
        }
    }
    
    /**
     * 根据用户 ID和图书 ID查找正在借阅的记录
     * 
     * @param userId 用户 ID
     * @param bookId 图书 ID
     * @return 借阅记录对象，如果不存在则返回null
     */
    public BorrowRecord findBorrowingRecord(Integer userId, Integer bookId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT br.*, u.username, u.real_name, b.book_name, b.author " +
                        "FROM borrow_records br " +
                        "JOIN users u ON br.user_id = u.user_id " +
                        "JOIN books b ON br.book_id = b.book_id " +
                        "WHERE br.user_id = ? AND br.book_id = ? " +
                        "AND (br.status = '借阅中' OR br.status = '已逾期') " +
                        "ORDER BY br.record_id DESC LIMIT 1";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, bookId);
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return extractBorrowRecordFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("查找正在借阅的记录失败：" + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeAll(connection, preparedStatement, resultSet);
        }
        
        return null;
    }
    
    /**
     * 从 ResultSet 中提取借阅记录对象
     * 
     * @param resultSet 结果集
     * @return 借阅记录对象
     * @throws SQLException SQL 异常
     */
    private BorrowRecord extractBorrowRecordFromResultSet(ResultSet resultSet) throws SQLException {
        BorrowRecord record = new BorrowRecord();
        record.setRecordId(resultSet.getInt("record_id"));
        record.setUserId(resultSet.getInt("user_id"));
        record.setBookId(resultSet.getInt("book_id"));
        record.setBorrowDate(resultSet.getTimestamp("borrow_date"));
        record.setDueDate(resultSet.getDate("due_date"));
        record.setReturnDate(resultSet.getTimestamp("return_date"));
        record.setStatus(resultSet.getString("status"));
        record.setRemarks(resultSet.getString("remarks"));
        record.setCreatedAt(resultSet.getTimestamp("created_at"));
        
        // 设置关联信息（如果存在）
        try {
            record.setUsername(resultSet.getString("username"));
            record.setRealName(resultSet.getString("real_name"));
            record.setBookName(resultSet.getString("book_name"));
            record.setAuthor(resultSet.getString("author"));
        } catch (SQLException e) {
            // 如果关联字段不存在，忽略异常
        }
        
        return record;
    }
}
