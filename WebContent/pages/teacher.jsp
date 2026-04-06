<%@ page import="java.util.List" %>
<%@ page import="com.school.model.User" %>
<%@ page import="com.school.model.Course" %>
<%@ page import="com.school.dao.CourseDAO" %>

<%
String path = request.getContextPath();
User teacher = (User) session.getAttribute("user");

if (teacher == null) {
    response.sendRedirect(path + "/pages/login.jsp");
    return;
}

String role = teacher.getRole();
if (role == null || !role.trim().equalsIgnoreCase("teacher")) {
    out.println("<h2>Access denied: only teacher can open this page.</h2>");
    return;
}

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

CourseDAO dao = new CourseDAO();
List<Course> courses = dao.getCoursesByTeacher(teacher.getId());
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Teacher Dashboard</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="navbar">
    <div class="brand">Teacher Panel</div>
    <div class="nav-links">
        <a href="<%= path %>/pages/teacher.jsp">Dashboard</a>
        <a href="<%= path %>/logout">Logout</a>
    </div>
</div>

<div class="page-header">
    <h1>Welcome, <%= teacher.getName() %></h1>
    <p>Open your assigned subjects and manage marks for students in your class.</p>
</div>

<div class="container">
    <div class="card">
        <h2>My Teaching Subjects</h2>

        <% if (courses == null || courses.isEmpty()) { %>
            <p>No subjects assigned to you yet.</p>
        <% } else { %>
            <table class="table">
                <tr>
                    <th>Course</th>
                    <th>Class</th>
                    <th>Action</th>
                </tr>

                <% for (Course c : courses) { %>
                <tr>
                    <td><%= c.getName() %></td>
                    <td><%= c.getClassName() == null ? "-" : c.getClassName() %></td>
                    <td>
                        <% if (c.getClassId() != null) { %>
                            <a class="btn-link small-btn"
                               href="<%= path %>/pages/teacherCourse.jsp?courseId=<%= c.getId() %>&classId=<%= c.getClassId() %>">
                               Open
                            </a>
                        <% } else { %>
                            -
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </table>
        <% } %>
    </div>
</div>

</body>
</html>