package com.school.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.school.dao.CourseDAO;
import com.school.model.User;

@WebServlet("/updateTeacherAssignment")
public class UpdateTeacherAssignmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User admin = session != null ? (User) session.getAttribute("user") : null;

        if (admin == null || !"admin".equalsIgnoreCase(admin.getRole())) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        try {
            int teacherId = Integer.parseInt(request.getParameter("teacherId"));
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int classId = Integer.parseInt(request.getParameter("classId"));

            CourseDAO dao = new CourseDAO();
            dao.clearTeacherAssignments(teacherId);
            dao.assignCourse(courseId, teacherId, classId);

            response.sendRedirect(request.getContextPath() + "/pages/teachers.jsp?updated=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/teachers.jsp?error=1");
        }
    }
}