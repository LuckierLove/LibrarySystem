package dev.ganziyan.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 借阅记录实体类
 * 对应数据库表：borrow_records
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {
    
    /**
     * 借阅记录ID（主键）
     */
    private Integer recordId;
    
    /**
     * 用户ID（外键）
     */
    private Integer userId;
    
    /**
     * 图书ID（外键）
     */
    private Integer bookId;
    
    /**
     * 借阅日期
     */
    private Timestamp borrowDate;
    
    /**
     * 应还日期
     */
    private Date dueDate;
    
    /**
     * 实际归还日期
     */
    private Timestamp returnDate;
    
    /**
     * 借阅状态（借阅中/已归还/已逾期）
     */
    private String status;
    
    /**
     * 备注信息
     */
    private String remarks;
    
    /**
     * 记录创建时间
     */
    private Timestamp createdAt;
    
    // 扩展字段：用于显示关联信息（不对应数据库字段）
    /**
     * 用户名（用于界面显示）
     */
    private String username;
    
    /**
     * 真实姓名（用于界面显示）
     */
    private String realName;
    
    /**
     * 图书名称（用于界面显示）
     */
    private String bookName;
    
    /**
     * 作者（用于界面显示）
     */
    private String author;
    
    /**
     * 构造方法：用于新建借阅记录
     */
    public BorrowRecord(Integer userId, Integer bookId, Date dueDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.dueDate = dueDate;
        this.status = "借阅中"; // 默认状态为借阅中
    }
    
    /**
     * 判断是否已归还
     * 
     * @return true表示已归还，false表示未归还
     */
    public boolean isReturned() {
        return "已归还".equals(this.status);
    }
    
    /**
     * 判断是否逾期
     * 
     * @return true表示已逾期，false表示未逾期
     */
    public boolean isOverdue() {
        return "已逾期".equals(this.status);
    }
    
    /**
     * 判断是否正在借阅中
     * 
     * @return true表示借阅中，false表示非借阅中
     */
    public boolean isBorrowing() {
        return "借阅中".equals(this.status) || "已逾期".equals(this.status);
    }
}
