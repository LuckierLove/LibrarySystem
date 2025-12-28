package cn.jju.library.service;

import cn.jju.library.dao.BookDao;
import cn.jju.library.dao.BorrowRecordDao;
import cn.jju.library.entity.Book;
import cn.jju.library.entity.BorrowRecord;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

/**
 * 借阅服务类
 * 实现借书、还书等核心业务逻辑
 * 
 * @author 图书管理系统开发组
 * @version 1.0.0
 */
public class BorrowService {
    
    private BookDao bookDao = new BookDao();
    private BorrowRecordDao borrowRecordDao = new BorrowRecordDao();
    
    /**
     * 借书操作
     * 业务规则：
     * 1. 检查图书库存是否充足（available_quantity > 0）
     * 2. 检查用户是否已经借阅该图书（避免重复借阅）
     * 3. 创建借阅记录
     * 4. 减少图书的可借数量
     * 
     * @param userId 用户ID
     * @param bookId 图书ID
     * @param borrowDays 借阅天数
     * @return 成功返回null，失败返回错误消息
     */
    public String borrowBook(Integer userId, Integer bookId, int borrowDays) {
        // 1. 查询图书信息
        Book book = bookDao.findById(bookId);
        if (book == null) {
            return "图书不存在！";
        }
        
        // 2. 检查图书库存
        if (!book.isAvailable()) {
            return "图书库存不足，暂时无法借阅！";
        }
        
        // 3. 检查用户是否已经借阅该图书（避免重复借阅）
        BorrowRecord existingRecord = borrowRecordDao.findBorrowingRecord(userId, bookId);
        if (existingRecord != null) {
            return "您已经借阅了这本书，请先归还后再借！";
        }
        
        // 4. 计算应还日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, borrowDays);
        Date dueDate = new Date(calendar.getTimeInMillis());
        
        // 5. 创建借阅记录
        BorrowRecord record = new BorrowRecord(userId, bookId, dueDate);
        boolean recordAdded = borrowRecordDao.add(record);
        if (!recordAdded) {
            return "创建借阅记录失败，请重试！";
        }
        
        // 6. 减少图书可借数量
        int newAvailableQuantity = book.getAvailableQuantity() - 1;
        boolean quantityUpdated = bookDao.updateAvailableQuantity(bookId, newAvailableQuantity);
        if (!quantityUpdated) {
            return "更新图书库存失败，请联系管理员！";
        }
        
        // 借书成功
        return null;
    }
    
    /**
     * 还书操作
     * 业务规则：
     * 1. 查找用户借阅该图书的记录
     * 2. 更新借阅记录状态为"已归还"
     * 3. 增加图书的可借数量
     * 
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return 成功返回null，失败返回错误消息
     */
    public String returnBook(Integer userId, Integer bookId) {
        // 1. 查找借阅记录
        BorrowRecord record = borrowRecordDao.findBorrowingRecord(userId, bookId);
        if (record == null) {
            return "未找到该图书的借阅记录！";
        }
        
        // 2. 更新借阅记录状态
        boolean recordUpdated = borrowRecordDao.returnBook(record.getRecordId());
        if (!recordUpdated) {
            return "更新借阅记录失败，请重试！";
        }
        
        // 3. 增加图书可借数量
        Book book = bookDao.findById(bookId);
        if (book != null) {
            int newAvailableQuantity = book.getAvailableQuantity() + 1;
            boolean quantityUpdated = bookDao.updateAvailableQuantity(bookId, newAvailableQuantity);
            if (!quantityUpdated) {
                return "更新图书库存失败，请联系管理员！";
            }
        }
        
        // 还书成功
        return null;
    }
    
    /**
     * 查询用户的所有借阅记录
     * 
     * @param userId 用户ID
     * @return 借阅记录列表
     */
    public List<BorrowRecord> getUserBorrowRecords(Integer userId) {
        return borrowRecordDao.findByUserId(userId);
    }
    
    /**
     * 查询用户当前正在借阅的图书
     * 
     * @param userId 用户ID
     * @return 正在借阅的记录列表
     */
    public List<BorrowRecord> getUserBorrowingRecords(Integer userId) {
        return borrowRecordDao.findBorrowingByUserId(userId);
    }
    
    /**
     * 查询所有借阅记录
     * 
     * @return 所有借阅记录列表
     */
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordDao.findAll();
    }
}
