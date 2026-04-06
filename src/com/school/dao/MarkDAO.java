package com.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.school.model.Mark;

public class MarkDAO {

    public boolean addMark(Mark mark) {
        boolean status = false;

        String checkSql = "SELECT id FROM marks WHERE student_id=? AND course_id=?";
        String insertSql = "INSERT INTO marks(student_id, course_id, score) VALUES (?, ?, ?)";
        String updateSql = "UPDATE marks SET score=? WHERE student_id=? AND course_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

            checkPs.setInt(1, mark.getStudentId());
            checkPs.setInt(2, mark.getCourseId());
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setInt(1, mark.getScore());
                    updatePs.setInt(2, mark.getStudentId());
                    updatePs.setInt(3, mark.getCourseId());
                    status = updatePs.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setInt(1, mark.getStudentId());
                    insertPs.setInt(2, mark.getCourseId());
                    insertPs.setInt(3, mark.getScore());
                    status = insertPs.executeUpdate() > 0;
                }
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean updateMark(Mark mark) {
        boolean status = false;
        String sql = "UPDATE marks SET score=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mark.getScore());
            ps.setInt(2, mark.getId());
            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public boolean deleteMark(int id) {
        boolean status = false;
        String sql = "DELETE FROM marks WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            status = ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public Mark getMarkById(int id) {
        Mark mark = null;
        String sql = "SELECT m.*, u.name AS student_name, c.name AS course_name " +
                     "FROM marks m " +
                     "JOIN users u ON m.student_id = u.id " +
                     "JOIN courses c ON m.course_id = c.id " +
                     "WHERE m.id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                mark = new Mark();
                mark.setId(rs.getInt("id"));
                mark.setStudentId(rs.getInt("student_id"));
                mark.setCourseId(rs.getInt("course_id"));
                mark.setScore(rs.getInt("score"));
                mark.setStudentName(rs.getString("student_name"));
                mark.setCourseName(rs.getString("course_name"));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mark;
    }

    public Mark getMarkByStudentAndCourse(int studentId, int courseId) {
        Mark mark = null;

        String sql = "SELECT m.*, u.name AS student_name, c.name AS course_name " +
                     "FROM marks m " +
                     "JOIN users u ON m.student_id = u.id " +
                     "JOIN courses c ON m.course_id = c.id " +
                     "WHERE m.student_id=? AND m.course_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                mark = new Mark();
                mark.setId(rs.getInt("id"));
                mark.setStudentId(rs.getInt("student_id"));
                mark.setCourseId(rs.getInt("course_id"));
                mark.setScore(rs.getInt("score"));
                mark.setStudentName(rs.getString("student_name"));
                mark.setCourseName(rs.getString("course_name"));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mark;
    }

    public List<Mark> getStudentsAndMarksForCourse(int courseId, int classId) {
        List<Mark> list = new ArrayList<Mark>();

        String sql = "SELECT u.id AS student_id, u.name AS student_name, m.id, m.score " +
                     "FROM users u " +
                     "LEFT JOIN marks m ON m.student_id = u.id AND m.course_id = ? " +
                     "WHERE u.role='student' AND u.class_id=? " +
                     "ORDER BY u.name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ps.setInt(2, classId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Mark m = new Mark();
                m.setId(rs.getInt("id"));
                m.setStudentId(rs.getInt("student_id"));
                m.setStudentName(rs.getString("student_name"));

                int score = rs.getInt("score");
                if (rs.wasNull()) {
                    score = 0;
                }
                m.setScore(score);
                list.add(m);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Mark> getMarksByStudentId(int studentId) {
        List<Mark> list = new ArrayList<Mark>();

        String sql = "SELECT m.*, c.name AS course_name " +
                     "FROM marks m " +
                     "JOIN courses c ON m.course_id = c.id " +
                     "WHERE m.student_id=? " +
                     "ORDER BY c.name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Mark mark = new Mark();
                mark.setId(rs.getInt("id"));
                mark.setStudentId(rs.getInt("student_id"));
                mark.setCourseId(rs.getInt("course_id"));
                mark.setScore(rs.getInt("score"));
                mark.setCourseName(rs.getString("course_name"));
                list.add(mark);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public double getAverageByStudentId(int studentId) {
        double average = 0;
        String sql = "SELECT AVG(score) FROM marks WHERE student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                average = rs.getDouble(1);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return average;
    }

    public double getAverageByCourse(int courseId) {
        double average = 0;
        String sql = "SELECT AVG(score) FROM marks WHERE course_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                average = rs.getDouble(1);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return average;
    }

    public int getStudentPosition(int studentId) {
        int position = 0;

        String sql =
            "SELECT ranked.student_id, ranked.position_no " +
            "FROM (" +
            "   SELECT student_id, " +
            "          DENSE_RANK() OVER (ORDER BY AVG(score) DESC) AS position_no " +
            "   FROM marks " +
            "   GROUP BY student_id" +
            ") ranked " +
            "WHERE ranked.student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                position = rs.getInt("position_no");
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return position;
    }

    public int countStudentsWithMarks() {
        int count = 0;
        String sql = "SELECT COUNT(DISTINCT student_id) FROM marks";

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

    public List<Mark> getTopStudents(int limit) {
        List<Mark> list = new ArrayList<Mark>();

        String sql =
            "SELECT m.student_id, u.name AS student_name, AVG(m.score) AS avg_score " +
            "FROM marks m " +
            "JOIN users u ON m.student_id = u.id " +
            "GROUP BY m.student_id, u.name " +
            "ORDER BY avg_score DESC " +
            "LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Mark mark = new Mark();
                mark.setStudentId(rs.getInt("student_id"));
                mark.setStudentName(rs.getString("student_name"));
                mark.setScore((int) Math.round(rs.getDouble("avg_score")));
                list.add(mark);
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countMarks() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM marks";

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