package com.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.school.model.Course;

public class CourseDAO {

    public boolean addCourse(Course course) {
        boolean status = false;
        String sql = "INSERT INTO courses(name, teacher_id, class_id) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getName());

            if (course.getTeacherId() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, course.getTeacherId());
            }

            if (course.getClassId() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, course.getClassId());
            }

            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean addCourseOnly(String name) {
        boolean status = false;
        String sql = "INSERT INTO courses(name, teacher_id, class_id) VALUES (?, NULL, NULL)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean assignCourse(int courseId, Integer teacherId, Integer classId) {
        boolean status = false;
        String sql = "UPDATE courses SET teacher_id=?, class_id=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (teacherId == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, teacherId);
            }

            if (classId == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, classId);
            }

            ps.setInt(3, courseId);
            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<Course>();
        String sql = "SELECT c.*, u.name AS teacher_name, cl.name AS class_name " +
                     "FROM courses c " +
                     "LEFT JOIN users u ON c.teacher_id = u.id " +
                     "LEFT JOIN classes cl ON c.class_id = cl.id " +
                     "ORDER BY c.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setTeacherId((Integer) rs.getObject("teacher_id"));
                c.setClassId((Integer) rs.getObject("class_id"));
                c.setTeacherName(rs.getString("teacher_name"));
                c.setClassName(rs.getString("class_name"));
                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Course> getCoursesByTeacher(int teacherId) {
        List<Course> list = new ArrayList<Course>();
        String sql = "SELECT c.*, cl.name AS class_name " +
                     "FROM courses c " +
                     "LEFT JOIN classes cl ON c.class_id = cl.id " +
                     "WHERE c.teacher_id=? " +
                     "ORDER BY c.name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setTeacherId((Integer) rs.getObject("teacher_id"));
                c.setClassId((Integer) rs.getObject("class_id"));
                c.setClassName(rs.getString("class_name"));
                list.add(c);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void clearTeacherAssignments(int teacherId) {
        String sql = "UPDATE courses SET teacher_id=NULL, class_id=NULL WHERE teacher_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Course getCourseById(int id) {
        Course c = null;
        String sql = "SELECT c.*, u.name AS teacher_name, cl.name AS class_name " +
                     "FROM courses c " +
                     "LEFT JOIN users u ON c.teacher_id=u.id " +
                     "LEFT JOIN classes cl ON c.class_id=cl.id " +
                     "WHERE c.id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new Course();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setTeacherId((Integer) rs.getObject("teacher_id"));
                c.setClassId((Integer) rs.getObject("class_id"));
                c.setTeacherName(rs.getString("teacher_name"));
                c.setClassName(rs.getString("class_name"));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    public boolean deleteCourse(int id) {
        boolean status = false;
        String sql = "DELETE FROM courses WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public int countCourses() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM courses";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}