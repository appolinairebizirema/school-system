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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null
                || email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp?error=1");
            return;
        }

        UserDAO dao = new UserDAO();
        User user = dao.loginUser(email.trim(), password.trim());

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            String role = user.getRole();
            if (role != null) {
                role = role.trim();
            } else {
                role = "";
            }

            if ("admin".equalsIgnoreCase(role)) {
                response.sendRedirect(request.getContextPath() + "/pages/admin.jsp");
            } else if ("teacher".equalsIgnoreCase(role)) {
                response.sendRedirect(request.getContextPath() + "/pages/teacher.jsp");
            } else if ("student".equalsIgnoreCase(role)) {
                response.sendRedirect(request.getContextPath() + "/pages/student.jsp");
            } else {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/pages/login.jsp?error=1");
            }

        } else {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp?error=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
    }
}