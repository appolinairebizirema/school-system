<%@ page import="java.util.List" %>
<%@ page import="com.school.model.User" %>
<%@ page import="com.school.model.Mark" %>
<%@ page import="com.school.dao.MarkDAO" %>

<%
String path = request.getContextPath();
User student = (User) session.getAttribute("user");

if (student == null || !"student".equalsIgnoreCase(student.getRole())) {
    response.sendRedirect(path + "/pages/login.jsp");
    return;
}

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

MarkDAO dao = new MarkDAO();
List<Mark> marks = dao.getMarksByStudentId(student.getId());
double average = dao.getAverageByStudentId(student.getId());
int position = dao.getStudentPosition(student.getId());
int totalRanked = dao.countStudentsWithMarks();

String grade = "-";
if (average >= 90) {
    grade = "A";
} else if (average >= 80) {
    grade = "B";
} else if (average >= 70) {
    grade = "C";
} else if (average >= 60) {
    grade = "D";
} else if (average > 0) {
    grade = "F";
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="navbar">
    <div class="brand">Student Panel</div>
    <div class="nav-links">
        <a href="<%= path %>/pages/student.jsp">Dashboard</a>
        <a href="<%= path %>/logout">Logout</a>
    </div>
</div>

<div class="page-header">
    <h1>Welcome, <%= student.getName() %></h1>
    <p>View your report, average, grade, and ranking.</p>
</div>

<div class="container">

    <div class="stats-grid">
        <div class="stat-card">
            <h3><%= String.format("%.2f", average) %></h3>
            <p>Average Score</p>
        </div>
        <div class="stat-card">
            <h3><%= grade %></h3>
            <p>Final Grade</p>
        </div>
        <div class="stat-card">
            <h3><%= position == 0 ? "-" : position %></h3>
            <p>Position</p>
        </div>
        <div class="stat-card">
            <h3><%= totalRanked %></h3>
            <p>Ranked Students</p>
        </div>
    </div>

    <div class="card">
        <h2>Report Card</h2>

        <% if (marks == null || marks.isEmpty()) { %>
            <p>No marks available yet.</p>
        <% } else { %>
            <table class="table">
                <tr>
                    <th>Course</th>
                    <th>Score</th>
                    <th>Grade</th>
                </tr>

                <% for (Mark m : marks) {
                    String rowGrade = "-";
                    if (m.getScore() >= 90) rowGrade = "A";
                    else if (m.getScore() >= 80) rowGrade = "B";
                    else if (m.getScore() >= 70) rowGrade = "C";
                    else if (m.getScore() >= 60) rowGrade = "D";
                    else if (m.getScore() > 0) rowGrade = "F";
                %>
                <tr>
                    <td><%= m.getCourseName() %></td>
                    <td><%= m.getScore() %></td>
                    <td><%= rowGrade %></td>
                </tr>
                <% } %>
            </table>
        <% } %>
    </div>

</div>

</body>
</html>