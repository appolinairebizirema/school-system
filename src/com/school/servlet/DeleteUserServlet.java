package com.school.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.school.dao.UserDAO;
import com.school.model.User;

@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {
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

            UserDAO dao = new UserDAO();
            dao.deleteUser(id);

            String referer = request.getHeader("Referer");
            if (referer != null && referer.contains("students.jsp")) {
                response.sendRedirect(request.getContextPath() + "/pages/students.jsp?deleted=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/teachers.jsp?deleted=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/admin.jsp?error=1");
        }
    }
}