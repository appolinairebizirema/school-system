package com.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.school.model.User;

public class UserDAO {

    public User loginUser(String email, String password) {
        User user = null;
        String sql = "SELECT u.*, c.name AS class_name " +
                     "FROM users u " +
                     "LEFT JOIN classes c ON u.class_id = c.id " +
                     "WHERE u.email=? AND u.password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setClassId((Integer) rs.getObject("class_id"));
                user.setClassName(rs.getString("class_name"));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean registerUser(User user) {
        boolean status = false;
        String sql = "INSERT INTO users(name, email, password, role, class_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            if (user.getClassId() == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, user.getClassId());
            }

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<User>();
        String sql = "SELECT u.*, c.name AS class_name " +
                     "FROM users u " +
                     "LEFT JOIN classes c ON u.class_id = c.id " +
                     "ORDER BY u.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setClassId((Integer) rs.getObject("class_id"));
                user.setClassName(rs.getString("class_name"));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<User> getUsersByRole(String role) {
        List<User> list = new ArrayList<User>();
        String sql = "SELECT u.*, c.name AS class_name " +
                     "FROM users u " +
                     "LEFT JOIN classes c ON u.class_id = c.id " +
                     "WHERE u.role=? " +
                     "ORDER BY u.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setClassId((Integer) rs.getObject("class_id"));
                user.setClassName(rs.getString("class_name"));
                list.add(user);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<User> getStudentsByClass(int classId) {
        List<User> list = new ArrayList<User>();
        String sql = "SELECT u.*, c.name AS class_name " +
                     "FROM users u " +
                     "LEFT JOIN classes c ON u.class_id = c.id " +
                     "WHERE u.role='student' AND u.class_id=? " +
                     "ORDER BY u.name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setClassId((Integer) rs.getObject("class_id"));
                user.setClassName(rs.getString("class_name"));
                list.add(user);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateStudentClass(int studentId, int classId) {
        boolean status = false;
        String sql = "UPDATE users SET class_id=? WHERE id=? AND role='student'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            ps.setInt(2, studentId);
            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT u.*, c.name AS class_name " +
                     "FROM users u " +
                     "LEFT JOIN classes c ON u.class_id = c.id " +
                     "WHERE u.id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setClassId((Integer) rs.getObject("class_id"));
                user.setClassName(rs.getString("class_name"));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean deleteUser(int id) {
        boolean status = false;
        String sql = "DELETE FROM users WHERE id=? AND role <> 'admin'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public int countUsersByRole(String role) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM users WHERE role=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<User> searchStudents(String keyword) {
        List<User> list = new ArrayList<User>();

        String sql = "SELECT u.*, c.name AS class_name " +
                     "FROM users u LEFT JOIN classes c ON u.class_id=c.id " +
                     "WHERE u.role='student' AND u.name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setClassId((Integer) rs.getObject("class_id"));
                u.setClassName(rs.getString("class_name"));
                list.add(u);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}