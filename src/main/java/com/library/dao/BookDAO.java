package com.library.dao;

import com.library.model.Book;
import com.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // ---------- CREATE ----------
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (isbn, title, author, publisher, category, publication_year, quantity, available_quantity) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getPublisher());
            ps.setString(5, book.getCategory());
            ps.setInt(6, book.getPublicationYear());
            ps.setInt(7, book.getQuantity());
            ps.setInt(8, book.getQuantity()); // available = quantity on creation

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- READ (all) ----------
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY id DESC";
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

    // ---------- READ (by id) ----------
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------- UPDATE ----------
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET isbn=?, title=?, author=?, publisher=?, category=?, " +
                     "publication_year=?, quantity=?, available_quantity=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getPublisher());
            ps.setString(5, book.getCategory());
            ps.setInt(6, book.getPublicationYear());
            ps.setInt(7, book.getQuantity());
            ps.setInt(8, book.getAvailableQuantity());
            ps.setInt(9, book.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- DELETE ----------
    public boolean deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- SEARCH ----------
    public List<Book> searchBooks(String keyword) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE id LIKE ? OR isbn LIKE ? OR title LIKE ? " +
                     "OR author LIKE ? OR category LIKE ? ORDER BY id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ps.setString(5, like);

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

    // ---------- Books available for issuing (available_quantity > 0) ----------
    public List<Book> getAvailableBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE available_quantity > 0 ORDER BY title ASC";
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

    // ---------- Counts for dashboard ----------
    public int getTotalBooksCount() {
        return singleCount("SELECT COALESCE(SUM(quantity),0) AS cnt FROM books");
    }

    public int getAvailableBooksCount() {
        return singleCount("SELECT COALESCE(SUM(available_quantity),0) AS cnt FROM books");
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

    /**
     * Increases or decreases available_quantity by delta (used when issuing/returning).
     * Prevents the value from going negative.
     */
    public boolean adjustAvailableQuantity(int bookId, int delta) {
        String sql = "UPDATE books SET available_quantity = available_quantity + ? " +
                     "WHERE id = ? AND available_quantity + ? >= 0";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, delta);
            ps.setInt(2, bookId);
            ps.setInt(3, delta);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setIsbn(rs.getString("isbn"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setPublisher(rs.getString("publisher"));
        b.setCategory(rs.getString("category"));
        b.setPublicationYear(rs.getInt("publication_year"));
        b.setQuantity(rs.getInt("quantity"));
        b.setAvailableQuantity(rs.getInt("available_quantity"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        return b;
    }
}
