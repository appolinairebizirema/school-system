package com.school.servlet;

import java.io.IOException;

import com.school.dao.CourseDAO;
import com.school.model.Course;
import com.school.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/addCourse")
public class AddCourseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedInUser = session != null ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null || !"admin".equalsIgnoreCase(loggedInUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        try {
            String name = request.getParameter("name");
            String teacherParam = request.getParameter("teacherId");
            String classParam = request.getParameter("classId");

            Integer teacherId = (teacherParam == null || teacherParam.trim().isEmpty())
                    ? null : Integer.parseInt(teacherParam);

            Integer classId = (classParam == null || classParam.trim().isEmpty())
                    ? null : Integer.parseInt(classParam);

            Course course = new Course(name, teacherId, classId);
            CourseDAO dao = new CourseDAO();

            if (dao.addCourse(course)) {
                response.sendRedirect(request.getContextPath() + "/pages/admin.jsp?courseSuccess=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/admin.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/admin.jsp?error=1");
        }
    }
}