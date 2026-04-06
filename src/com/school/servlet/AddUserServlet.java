package com.school.servlet;

import java.io.IOException;

import com.school.dao.UserDAO;
import com.school.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedInUser = session != null ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null || !"admin".equalsIgnoreCase(loggedInUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String classIdParam = request.getParameter("classId");

        Integer classId = null;
        if ("student".equalsIgnoreCase(role) && classIdParam != null && !classIdParam.isEmpty()) {
            classId = Integer.parseInt(classIdParam);
        }

        User user = new User(name, email, password, role, classId);
        UserDAO dao = new UserDAO();

        if (dao.registerUser(user)) {
            response.sendRedirect(request.getContextPath() + "/pages/admin.jsp?success=1");
        } else {
            response.sendRedirect(request.getContextPath() + "/pages/admin.jsp?error=1");
        }
    }
}