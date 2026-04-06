<%@ page import="java.util.List" %>
<%@ page import="com.school.model.User" %>
<%@ page import="com.school.model.Mark" %>
<%@ page import="com.school.dao.UserDAO" %>
<%@ page import="com.school.dao.MarkDAO" %>

<%
String path = request.getContextPath();
User teacher = (User) session.getAttribute("user");

if (teacher == null || !"teacher".equalsIgnoreCase(teacher.getRole())) {
    response.sendRedirect(path + "/pages/login.jsp");
    return;
}

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

String courseIdParam = request.getParameter("courseId");
String classIdParam = request.getParameter("classId");

if (courseIdParam == null || classIdParam == null) {
    response.sendRedirect(path + "/pages/teacher.jsp");
    return;
}

int courseId = Integer.parseInt(courseIdParam);
int classId = Integer.parseInt(classIdParam);

UserDAO userDao = new UserDAO();
MarkDAO markDao = new MarkDAO();

List<User> students = userDao.getStudentsByClass(classId);
double courseAverage = markDao.getAverageByCourse(courseId);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Teacher Course</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="navbar">
    <div class="brand">Course Students</div>
    <div class="nav-links">
        <a href="<%= path %>/pages/teacher.jsp">Dashboard</a>
        <a href="<%= path %>/logout">Logout</a>
    </div>
</div>

<div class="page-header">
    <h1>Students in This Class</h1>
    <p>Add marks, edit results, and monitor class performance.</p>
</div>

<div class="container">

    <div class="stats-grid two-col">
        <div class="stat-card">
            <h3><%= students == null ? 0 : students.size() %></h3>
            <p>Total Students</p>
        </div>
        <div class="stat-card">
            <h3><%= String.format("%.2f", courseAverage) %></h3>
            <p>Course Average</p>
        </div>
    </div>

    <% if (request.getParameter("success") != null) { %>
        <p class="success">Mark saved successfully.</p>
    <% } %>
    <% if (request.getParameter("updated") != null) { %>
        <p class="success">Mark updated successfully.</p>
    <% } %>
    <% if (request.getParameter("error") != null) { %>
        <p class="error">Operation failed.</p>
    <% } %>

    <div class="card">
        <h2>Add Student Mark</h2>

        <form action="<%= path %>/addMark" method="post" class="form-grid">
            <input type="hidden" name="courseId" value="<%= courseId %>">
            <input type="hidden" name="classId" value="<%= classId %>">

            <select name="studentId" required>
                <option value="">Select student</option>
                <% for (User s : students) { %>
                    <option value="<%= s.getId() %>"><%= s.getName() %></option>
                <% } %>
            </select>

            <input type="number" name="score" placeholder="Enter marks" min="0" max="100" required>
            <button type="submit">Save Mark</button>
        </form>
    </div>

    <div class="card">
        <h2>Students List</h2>

        <% if (students == null || students.isEmpty()) { %>
            <p>No students found in this class.</p>
        <% } else { %>
            <table class="table">
                <tr>
                    <th>Student</th>
                    <th>Current Mark</th>
                    <th>Grade</th>
                    <th>Action</th>
                </tr>

                <% for (User s : students) {
                    Mark m = markDao.getMarkByStudentAndCourse(s.getId(), courseId);
                    int score = (m == null ? 0 : m.getScore());
                    String grade = "-";

                    if (score >= 90) grade = "A";
                    else if (score >= 80) grade = "B";
                    else if (score >= 70) grade = "C";
                    else if (score >= 60) grade = "D";
                    else if (score > 0) grade = "F";
                %>
                <tr>
                    <td><%= s.getName() %></td>
                    <td><%= score %></td>
                    <td><%= grade %></td>
                    <td>
                        <% if (m != null) { %>
                            <a class="btn-link small-btn"
                               href="<%= path %>/pages/editMark.jsp?id=<%= m.getId() %>&courseId=<%= courseId %>&classId=<%= classId %>">
                               Edit
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