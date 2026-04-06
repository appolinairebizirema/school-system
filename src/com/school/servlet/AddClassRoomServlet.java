package com.school.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.school.dao.ClassRoomDAO;
import com.school.model.ClassRoom;
import com.school.model.User;

@WebServlet("/addClassRoom")
public class AddClassRoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return;
        }

        String name = request.getParameter("name");

        ClassRoomDAO dao = new ClassRoomDAO();
        boolean status = dao.addClassRoom(new ClassRoom(name));

        if (status) {
            response.sendRedirect(request.getContextPath() + "/pages/classes.jsp?success=1");
        } else {
            response.sendRedirect(request.getContextPath() + "/pages/classes.jsp?error=1");
        }
    }
}