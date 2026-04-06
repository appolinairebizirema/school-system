package com.school.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.school.dao.CourseDAO;
import com.school.dao.UserDAO;
import com.school.model.User;

@WebServlet("/addTeacher")
public class AddTeacherServlet extends HttpServlet {
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
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int classId = Integer.parseInt(request.getParameter("classId"));

            UserDAO userDao = new UserDAO();
            User teacher = new User(name, username, password, "teacher", null);

            boolean saved = userDao.registerUser(teacher);

            if (saved) {
                User createdTeacher = userDao.loginUser(username, password);
                if (createdTeacher != null) {
                    CourseDAO courseDao = new CourseDAO();
                    courseDao.assignCourse(courseId, createdTeacher.getId(), classId);
                }
                response.sendRedirect(request.getContextPath() + "/pages/teachers.jsp?success=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/teachers.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/teachers.jsp?error=1");
        }
    }
}