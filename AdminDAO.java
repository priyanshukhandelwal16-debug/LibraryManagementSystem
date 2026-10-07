package com.library.dao;

import com.library.model.Admin;
import com.library.util.DBConnection;

import java.sql.*;

public class AdminDAO {

    /**
     * Validates login credentials against the admin table.
     * Returns the Admin object if valid, otherwise null.
     */
    public Admin validateLogin(String username, String password) {
        String sql = "SELECT id, username, password FROM admin WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setPassword(rs.getString("password"));
                    return admin;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
