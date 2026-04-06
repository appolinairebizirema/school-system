<%@ page import="java.util.List" %>
<%@ page import="com.school.model.User" %>
<%@ page import="com.school.model.Course" %>
<%@ page import="com.school.model.ClassRoom" %>
<%@ page import="com.school.dao.UserDAO" %>
<%@ page import="com.school.dao.CourseDAO" %>
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
CourseDAO courseDao = new CourseDAO();
ClassRoomDAO classDao = new ClassRoomDAO();

List<User> teachers = userDao.getUsersByRole("teacher");
List<Course> courses = courseDao.getAllCourses();
List<ClassRoom> classes = classDao.getAllClasses();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Teachers Management</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="navbar">
    <div class="brand">Teachers</div>
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
    <h1>Teachers Management</h1>
    <p>Add teachers, assign them courses and classes, and manage teacher records.</p>
</div>

<div class="container">

    <% if (request.getParameter("success") != null) { %>
        <p class="success">Teacher added successfully.</p>
    <% } %>

    <% if (request.getParameter("updated") != null) { %>
        <p class="success">Teacher updated successfully.</p>
    <% } %>

    <% if (request.getParameter("deleted") != null) { %>
        <p class="success">Teacher removed successfully.</p>
    <% } %>

    <% if (request.getParameter("error") != null) { %>
        <p class="error">Operation failed.</p>
    <% } %>

    <div class="card">
        <h2>Add Teacher</h2>

        <form action="<%= path %>/addTeacher" method="post" class="form-grid">
            <input type="text" name="name" placeholder="Teacher full names" required>
            <input type="text" name="username" placeholder="Username / email" required>
            <input type="password" name="password" placeholder="Password" required>

            <select name="courseId" required>
                <option value="">Select course</option>
                <% for (Course c : courses) { %>
                    <option value="<%= c.getId() %>"><%= c.getName() %></option>
                <% } %>
            </select>

            <select name="classId" required>
                <option value="">Select class</option>
                <% for (ClassRoom cl : classes) { %>
                    <option value="<%= cl.getId() %>"><%= cl.getName() %></option>
                <% } %>
            </select>

            <button type="submit">Add Teacher</button>
        </form>
    </div>

    <div class="card">
        <h2>All Teachers</h2>

        <% if (teachers == null || teachers.isEmpty()) { %>
            <p>No teachers found.</p>
        <% } else { %>
            <table class="table">
                <tr>
                    <th>ID</th>
                    <th>Names</th>
                    <th>Username</th>
                    <th>Lessons</th>
                    <th>Class</th>
                    <th>Action</th>
                </tr>

                <% for (User t : teachers) {
                    List<Course> teacherCourses = courseDao.getCoursesByTeacher(t.getId());
                    String lessons = "";
                    String className = "-";

                    for (int i = 0; i < teacherCourses.size(); i++) {
                        lessons += teacherCourses.get(i).getName();
                        if (teacherCourses.get(i).getClassName() != null) {
                            className = teacherCourses.get(i).getClassName();
                        }
                        if (i < teacherCourses.size() - 1) {
                            lessons += ", ";
                        }
                    }
                %>
                <tr>
                    <td><%= t.getId() %></td>
                    <td><%= t.getName() %></td>
                    <td><%= t.getEmail() %></td>
                    <td><%= lessons.isEmpty() ? "-" : lessons %></td>
                    <td><%= className %></td>
                    <td>
                        <div class="action-row">
                            <a class="btn-link small-btn" href="<%= path %>/pages/editTeacher.jsp?id=<%= t.getId() %>">Edit</a>
                            <form action="<%= path %>/deleteUser" method="post" onsubmit="return confirm('Remove this teacher?');">
                                <input type="hidden" name="id" value="<%= t.getId() %>">
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