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

@WebServlet("/addCourseOnly")
public class AddCourseOnlyServlet extends HttpServlet {
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
            String name = request.getParameter("name");

            if (name == null || name.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/pages/courses.jsp?error=1");
                return;
            }

            CourseDAO dao = new CourseDAO();
            boolean status = dao.addCourseOnly(name.trim());

            if (status) {
                response.sendRedirect(request.getContextPath() + "/pages/courses.jsp?courseOnlySuccess=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/courses.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/courses.jsp?error=1");
        }
    }
}