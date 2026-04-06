package com.school.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.school.dao.MarkDAO;
import com.school.model.Mark;
import com.school.model.User;

@WebServlet("/addMark")
public class AddMarkServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User teacher = session != null ? (User) session.getAttribute("user") : null;

        if (teacher == null || !"teacher".equalsIgnoreCase(teacher.getRole())) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        try {
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int classId = Integer.parseInt(request.getParameter("classId"));
            int score = Integer.parseInt(request.getParameter("score"));

            Mark mark = new Mark(studentId, courseId, score);
            MarkDAO dao = new MarkDAO();
            dao.addMark(mark);

            response.sendRedirect(request.getContextPath()
                    + "/pages/teacherCourse.jsp?courseId=" + courseId
                    + "&classId=" + classId
                    + "&success=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/teacher.jsp?error=1");
        }
    }
}