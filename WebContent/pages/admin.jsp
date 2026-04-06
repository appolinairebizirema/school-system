<%@ page import="java.util.List" %>
<%@ page import="com.school.model.User" %>
<%@ page import="com.school.model.Mark" %>
<%@ page import="com.school.dao.UserDAO" %>
<%@ page import="com.school.dao.CourseDAO" %>
<%@ page import="com.school.dao.MarkDAO" %>

<%
String path = request.getContextPath();
User user = (User) session.getAttribute("user");

if (user == null || user.getRole() == null || !user.getRole().trim().equalsIgnoreCase("admin")) {
    response.sendRedirect(path + "/pages/login.jsp");
    return;
}

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);

UserDAO userDao = new UserDAO();
CourseDAO courseDao = new CourseDAO();
MarkDAO markDao = new MarkDAO();

int totalTeachers = userDao.countUsersByRole("teacher");
int totalStudents = userDao.countUsersByRole("student");
int totalCourses = courseDao.countCourses();
int totalMarks = markDao.countMarks();

List<Mark> topStudents = markDao.getTopStudents(5);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">

    <style>
        .admin-stats-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 18px;
            margin-bottom: 24px;
        }

        .admin-stat-card {
            background: #ffffff;
            padding: 24px;
            border-radius: 14px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            text-align: center;
        }

        .admin-stat-card h3 {
            font-size: 34px;
            color: #1e3a8a;
            margin-bottom: 8px;
        }

        .admin-stat-card p {
            color: #64748b;
            font-size: 15px;
        }

        .admin-dashboard-grid {
            display: grid;
            grid-template-columns: 1.2fr 1fr;
            gap: 20px;
        }

        .chart-box {
            background: #ffffff;
            padding: 24px;
            border-radius: 14px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        }

        .chart-box h2 {
            margin-bottom: 18px;
        }

        .chart-item {
            margin-bottom: 18px;
        }

        .chart-head {
            display: flex;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 8px;
            font-size: 15px;
            font-weight: 600;
            color: #334155;
        }

        .chart-track {
            width: 100%;
            height: 28px;
            background: #e2e8f0;
            border-radius: 999px;
            overflow: hidden;
        }

        .chart-fill {
            height: 100%;
            min-width: 40px;
            background: linear-gradient(90deg, #2563eb, #60a5fa);
            color: white;
            font-size: 13px;
            font-weight: bold;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            padding-right: 10px;
            border-radius: 999px;
        }

        .admin-table-card {
            background: #ffffff;
            padding: 24px;
            border-radius: 14px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        }

        .quick-actions-card {
            background: #ffffff;
            padding: 24px;
            border-radius: 14px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            margin-bottom: 20px;
        }

        .quick-actions-card h2 {
            margin-bottom: 14px;
        }

        .quick-actions-row {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
        }

        .quick-actions-row a {
            display: inline-block;
            background: #2563eb;
            color: white;
            text-decoration: none;
            padding: 10px 14px;
            border-radius: 8px;
            font-weight: 600;
        }

        .quick-actions-row a:hover {
            background: #1d4ed8;
        }

        @media (max-width: 950px) {
            .admin-stats-grid {
                grid-template-columns: repeat(2, 1fr);
            }

            .admin-dashboard-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 600px) {
            .admin-stats-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

<div class="navbar">
    <div class="brand">School Admin</div>
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
    <h1>Welcome, <%= user.getName() %></h1>
    <p>Monitor school activity, student performance, and key totals.</p>
</div>

<div class="container">

    <div class="admin-stats-grid">
        <div class="admin-stat-card">
            <h3><%= totalTeachers %></h3>
            <p>Total Teachers</p>
        </div>
        <div class="admin-stat-card">
            <h3><%= totalStudents %></h3>
            <p>Total Students</p>
        </div>
        <div class="admin-stat-card">
            <h3><%= totalCourses %></h3>
            <p>Total Courses</p>
        </div>
        <div class="admin-stat-card">
            <h3><%= totalMarks %></h3>
            <p>Total Marks</p>
        </div>
    </div>

    <div class="quick-actions-card">
        <h2>Quick Actions</h2>
        <div class="quick-actions-row">
            <a href="<%= path %>/pages/teachers.jsp">Manage Teachers</a>
            <a href="<%= path %>/pages/students.jsp">Manage Students</a>
            <a href="<%= path %>/pages/courses.jsp">Manage Courses</a>
            <a href="<%= path %>/pages/classes.jsp">Manage Classes</a>
        </div>
    </div>

    <div class="admin-dashboard-grid">

        <div class="chart-box">
            <h2>Top Students Chart</h2>

            <% if (topStudents == null || topStudents.isEmpty()) { %>
                <p>No ranking data available yet.</p>
            <% } else { %>
                <% for (int i = 0; i < topStudents.size(); i++) {
                    Mark m = topStudents.get(i);
                    int avg = m.getScore();
                    if (avg < 0) avg = 0;
                    if (avg > 100) avg = 100;
                %>
                    <div class="chart-item">
                        <div class="chart-head">
                            <span><%= (i + 1) %>. <%= m.getStudentName() %></span>
                            <span><%= avg %>%</span>
                        </div>
                        <div class="chart-track">
                            <div class="chart-fill" style="width: <%= avg %>%;">
                                <%= avg %>%
                            </div>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </div>

        <div class="admin-table-card">
            <h2>Top Students Table</h2>

            <% if (topStudents == null || topStudents.isEmpty()) { %>
                <p>No student performance data yet.</p>
            <% } else { %>
                <table class="table">
                    <tr>
                        <th>Position</th>
                        <th>Student</th>
                        <th>Average</th>
                    </tr>
                    <% for (int i = 0; i < topStudents.size(); i++) { %>
                    <tr>
                        <td><%= i + 1 %></td>
                        <td><%= topStudents.get(i).getStudentName() %></td>
                        <td><%= topStudents.get(i).getScore() %></td>
                    </tr>
                    <% } %>
                </table>
            <% } %>
        </div>

    </div>

</div>

</body>
</html>