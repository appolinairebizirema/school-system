package com.school.servlet;

import java.io.IOException;

import com.school.dao.MarkDAO;
import com.school.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/deleteMark")
public class DeleteMarkServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedInUser = session != null ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null || !"teacher".equalsIgnoreCase(loggedInUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            MarkDAO dao = new MarkDAO();

            if (dao.deleteMark(id)) {
                response.sendRedirect(request.getContextPath() + "/pages/teacher.jsp?deleted=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/pages/teacher.jsp?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/pages/teacher.jsp?error=1");
        }
    }
}