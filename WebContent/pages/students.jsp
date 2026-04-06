<%@ page import="java.util.List" %>
<%@ page import="com.school.model.User" %>
<%@ page import="com.school.model.ClassRoom" %>
<%@ page import="com.school.dao.UserDAO" %>
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

UserDAO userDao = new UserDAO();
ClassRoomDAO classDao = new ClassRoomDAO();

List<User> students = userDao.getUsersByRole("student");
List<ClassRoom> classes = classDao.getAllClasses();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Students Management</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="navbar">
    <div class="brand">Students</div>
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
    <h1>Students Management</h1>
    <p>Register students, assign them to classes, and manage student records.</p>
</div>

<div class="container">

    <% if (request.getParameter("success") != null) { %>
        <p class="success">Student added successfully.</p>
    <% } %>

    <% if (request.getParameter("updated") != null) { %>
        <p class="success">Student updated successfully.</p>
    <% } %>

    <% if (request.getParameter("deleted") != null) { %>
        <p class="success">Student removed successfully.</p>
    <% } %>

    <% if (request.getParameter("error") != null) { %>
        <p class="error">Operation failed.</p>
    <% } %>

    <div class="card">
        <h2>Add Student</h2>

        <form action="<%= path %>/addStudent" method="post" class="form-grid">
            <input type="text" name="name" placeholder="Student full names" required>
            <input type="text" name="username" placeholder="Username / email" required>
            <input type="password" name="password" placeholder="Password" required>

            <select name="classId" required>
                <option value="">Select class</option>
                <% for (ClassRoom cl : classes) { %>
                    <option value="<%= cl.getId() %>"><%= cl.getName() %></option>
                <% } %>
            </select>

            <button type="submit">Add Student</button>
        </form>
    </div>

    <div class="card">
        <h2>All Students</h2>

        <% if (students == null || students.isEmpty()) { %>
            <p>No students found.</p>
        <% } else { %>
            <table class="table">
                <tr>
                    <th>ID</th>
                    <th>Names</th>
                    <th>Username</th>
                    <th>Class</th>
                    <th>Action</th>
                </tr>

                <% for (User s : students) { %>
                <tr>
                    <td><%= s.getId() %></td>
                    <td><%= s.getName() %></td>
                    <td><%= s.getEmail() %></td>
                    <td><%= s.getClassName() == null ? "-" : s.getClassName() %></td>
                    <td>
                        <div class="action-row">
                            <a class="btn-link small-btn" href="<%= path %>/pages/editStudent.jsp?id=<%= s.getId() %>">Edit</a>
                            <form action="<%= path %>/deleteUser" method="post" onsubmit="return confirm('Remove this student?');">
                                <input type="hidden" name="id" value="<%= s.getId() %>">
                                <button type="submit" class="danger small-btn">Remove</button>
                            </form>
                        </div>
                    </td>
                </tr>
                <% } %>
            </table>
        <% } %>
    </div>

</div>

</body>
</html>