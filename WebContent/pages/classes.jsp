<%@ page import="java.util.List" %>
<%@ page import="com.school.model.User" %>
<%@ page import="com.school.model.ClassRoom" %>
<%@ page import="com.school.dao.ClassRoomDAO" %>

<%
String path = request.getContextPath();
User admin = (User) session.getAttribute("user");

if (admin == null || !"admin".equalsIgnoreCase(admin.getRole())) {
    response.sendRedirect(path + "/pages/login.jsp");
    return;
}

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

ClassRoomDAO classDao = new ClassRoomDAO();
List<ClassRoom> classes = classDao.getAllClasses();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Classes Management</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="navbar">
    <div class="brand">Classes</div>
    <div class="nav-links">
        <a href="<%= path %>/pages/admin.jsp">Dashboard</a>
        <a href="<%= path %>/pages/teachers.jsp">Teachers</a>
        <a href="<%= path %>/pages/students.jsp">Students</a>
        <a href="<%= path %>/pages/courses.jsp">Courses</a>
        <a href="<%= path %>/pages/classes.jsp">Classes</a>
        <a href="<%= path %>/logout">Logout</a>
    </div>
</div>

<div class="page-header">
    <h1>Classes Management</h1>
    <p>Create and manage class groups.</p>
</div>

<div class="container">
    <% if (request.getParameter("success") != null || request.getParameter("classSuccess") != null) { %>
        <p class="success">Class added successfully.</p>
    <% } %>
    <% if (request.getParameter("error") != null) { %>
        <p class="error">Operation failed.</p>
    <% } %>

    <div class="card">
        <h2>Add Class</h2>
        <form action="<%= path %>/addClassRoom" method="post" class="form-grid">
            <input type="text" name="name" placeholder="Class name (example: Senior 5 A)" required>
            <button type="submit">Add Class</button>
        </form>
    </div>

    <div class="card">
        <h2>All Classes</h2>

        <% if (classes == null || classes.isEmpty()) { %>
            <p>No classes available.</p>
        <% } else { %>
            <table class="table">
                <tr>
                    <th>ID</th>
                    <th>Class Name</th>
                </tr>

                <% for (ClassRoom cl : classes) { %>
                <tr>
                    <td><%= cl.getId() %></td>
                    <td><%= cl.getName() %></td>
                </tr>
                <% } %>
            </table>
        <% } %>
    </div>
</div>

</body>
</html>