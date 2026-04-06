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

@WebServlet("/deleteCourse")
public class DeleteCourseServlet extends HttpServlet {
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
            int id = Integer.parseInt(request.getParameter("id"));
            CourseDAO dao = new CourseDAO();

            if (dao.deleteCourse(id)) {
                response.sendRedirect(request.getContextPath() + "/pages/courses.jsp?deletedCourse=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/courses.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/courses.jsp?error=1");
        }
    }
}