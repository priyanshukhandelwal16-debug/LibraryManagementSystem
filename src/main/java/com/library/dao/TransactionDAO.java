package com.library.dao;

import com.library.model.Transaction;
import com.library.util.DBConnection;
import com.library.util.FineConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /**
     * Issues a book to a member.
     * Steps performed (as a single DB transaction):
     *  1. Lock and check the book's available_quantity.
     *  2. If > 0, insert a new transaction row with status ISSUED.
     *  3. Decrease available_quantity by 1.
     * Returns true only if both steps succeed.
     */
    public String issueBook(int memberId, int bookId, Date issueDate, Date dueDate) {
        String checkSql = "SELECT available_quantity FROM books WHERE id = ? FOR UPDATE";
        String insertSql = "INSERT INTO transactions (member_id, book_id, issue_date, due_date, status) " +
                            "VALUES (?, ?, ?, ?, 'ISSUED')";
        String updateBookSql = "UPDATE books SET available_quantity = available_quantity - 1 WHERE id = ? AND available_quantity > 0";

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int available = 0;
            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, bookId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        available = rs.getInt("available_quantity");
                    }
                }
            }

            if (available <= 0) {
                con.rollback();
                return "UNAVAILABLE";
            }

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, memberId);
                ps.setInt(2, bookId);
                ps.setDate(3, issueDate);
                ps.setDate(4, dueDate);
                ps.executeUpdate();
            }

            int rowsUpdated;
            try (PreparedStatement ps = con.prepareStatement(updateBookSql)) {
                ps.setInt(1, bookId);
                rowsUpdated = ps.executeUpdate();
            }

            if (rowsUpdated == 0) {
                con.rollback();
                return "UNAVAILABLE";
            }

            con.commit();
            return "SUCCESS";

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return "ERROR";
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    /**
     * Returns a book.
     *  1. Verify the transaction exists and is still ISSUED (not already returned).
     *  2. Calculate fine based on due date vs today.
     *  3. Update transaction: return_date, fine, status = RETURNED.
     *  4. Increase book's available_quantity by 1.
     */
    public String returnBook(int transactionId) {
        String getSql = "SELECT book_id, due_date, status FROM transactions WHERE id = ? FOR UPDATE";
        String updateTxnSql = "UPDATE transactions SET return_date = ?, fine = ?, status = 'RETURNED' WHERE id = ? AND status != 'RETURNED'";
        String updateBookSql = "UPDATE books SET available_quantity = available_quantity + 1 WHERE id = ?";

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int bookId = -1;
            Date dueDate = null;
            String currentStatus = null;

            try (PreparedStatement ps = con.prepareStatement(getSql)) {
                ps.setInt(1, transactionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        bookId = rs.getInt("book_id");
                        dueDate = rs.getDate("due_date");
                        currentStatus = rs.getString("status");
                    }
                }
            }

            if (bookId == -1) {
                con.rollback();
                return "NOT_FOUND";
            }
            if ("RETURNED".equalsIgnoreCase(currentStatus)) {
                con.rollback();
                return "ALREADY_RETURNED";
            }

            LocalDate today = LocalDate.now();
            LocalDate due = dueDate.toLocalDate();
            long lateDays = ChronoUnit.DAYS.between(due, today);
            BigDecimal fine = BigDecimal.ZERO;
            if (lateDays > 0) {
                fine = BigDecimal.valueOf(lateDays * FineConfig.FINE_PER_DAY);
            }

            Date returnDate = Date.valueOf(today);

            int updated;
            try (PreparedStatement ps = con.prepareStatement(updateTxnSql)) {
                ps.setDate(1, returnDate);
                ps.setBigDecimal(2, fine);
                ps.setInt(3, transactionId);
                updated = ps.executeUpdate();
            }

            if (updated == 0) {
                con.rollback();
                return "ALREADY_RETURNED";
            }

            try (PreparedStatement ps = con.prepareStatement(updateBookSql)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            con.commit();
            return "SUCCESS";

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return "ERROR";
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    /** Marks ISSUED transactions whose due_date has passed as OVERDUE (status stays informational; fine is computed at return time). */
    public void refreshOverdueStatuses() {
        // TRUNC(SYSDATE) = Oracle equivalent of MySQL's CURDATE() (today's date, no time part)
        String sql = "UPDATE transactions SET status = 'OVERDUE' WHERE status = 'ISSUED' AND due_date < TRUNC(SYSDATE)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String JOIN_SELECT =
            "SELECT t.*, m.full_name AS member_name, b.title AS book_title " +
            "FROM transactions t " +
            "JOIN members m ON t.member_id = m.id " +
            "JOIN books b ON t.book_id = b.id ";

    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = JOIN_SELECT + "ORDER BY t.id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Transaction> getTransactionsByStatus(String status) {
        List<Transaction> list = new ArrayList<>();
        String sql = JOIN_SELECT + "WHERE t.status = ? ORDER BY t.id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Currently issued/overdue books, used on the Return Book page */
    public List<Transaction> getActiveTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = JOIN_SELECT + "WHERE t.status IN ('ISSUED','OVERDUE') ORDER BY t.due_date ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Transaction> searchTransactions(String keyword) {
        List<Transaction> list = new ArrayList<>();
        String sql = JOIN_SELECT + "WHERE m.full_name LIKE ? OR b.title LIKE ? OR t.status LIKE ? ORDER BY t.id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getIssuedCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM transactions WHERE status IN ('ISSUED','OVERDUE')";
        return singleCount(sql);
    }

    public int getReturnedCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM transactions WHERE status = 'RETURNED'";
        return singleCount(sql);
    }

    private int singleCount(String sql) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("cnt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Transaction> getRecentTransactions(int limit) {
        List<Transaction> list = new ArrayList<>();
        // "FETCH FIRST ? ROWS ONLY" is the Oracle (ANSI SQL) equivalent of MySQL's "LIMIT ?"
        String sql = JOIN_SELECT + "ORDER BY t.id DESC FETCH FIRST ? ROWS ONLY";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getInt("id"));
        t.setMemberId(rs.getInt("member_id"));
        t.setBookId(rs.getInt("book_id"));
        t.setIssueDate(rs.getDate("issue_date"));
        t.setDueDate(rs.getDate("due_date"));
        t.setReturnDate(rs.getDate("return_date"));
        t.setFine(rs.getBigDecimal("fine"));
        t.setStatus(rs.getString("status"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setMemberName(rs.getString("member_name"));
        t.setBookTitle(rs.getString("book_title"));
        return t;
    }
}
