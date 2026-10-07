package com.library.dao;

import com.library.model.Member;
import com.library.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    // ---------- CREATE ----------
    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (member_id, full_name, email, phone, gender, course, semester, address, registration_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, member.getMemberId());
            ps.setString(2, member.getFullName());
            ps.setString(3, member.getEmail());
            ps.setString(4, member.getPhone());
            ps.setString(5, member.getGender());
            ps.setString(6, member.getCourse());
            ps.setString(7, member.getSemester());
            ps.setString(8, member.getAddress());
            ps.setDate(9, member.getRegistrationDate());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Generates the next member_id like MEM1001, MEM1002 ... */
    public String generateNextMemberId() {
        String sql = "SELECT member_id FROM members ORDER BY id DESC FETCH FIRST 1 ROW ONLY";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String last = rs.getString("member_id"); // e.g. MEM1004
                int num = Integer.parseInt(last.replaceAll("\\D+", ""));
                return "MEM" + (num + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "MEM1001";
    }

    // ---------- READ (all) ----------
    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY id DESC";
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
    public Member getMemberById(int id) {
        String sql = "SELECT * FROM members WHERE id = ?";
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
    public boolean updateMember(Member member) {
        String sql = "UPDATE members SET full_name=?, email=?, phone=?, gender=?, course=?, " +
                     "semester=?, address=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, member.getFullName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getGender());
            ps.setString(5, member.getCourse());
            ps.setString(6, member.getSemester());
            ps.setString(7, member.getAddress());
            ps.setInt(8, member.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- DELETE ----------
    public boolean deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id = ?";
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
    public List<Member> searchMembers(String keyword) {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE member_id LIKE ? OR full_name LIKE ? OR email LIKE ? " +
                     "ORDER BY id DESC";
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

    public int getTotalMembersCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM members";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("cnt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getInt("id"));
        m.setMemberId(rs.getString("member_id"));
        m.setFullName(rs.getString("full_name"));
        m.setEmail(rs.getString("email"));
        m.setPhone(rs.getString("phone"));
        m.setGender(rs.getString("gender"));
        m.setCourse(rs.getString("course"));
        m.setSemester(rs.getString("semester"));
        m.setAddress(rs.getString("address"));
        m.setRegistrationDate(rs.getDate("registration_date"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }
}
