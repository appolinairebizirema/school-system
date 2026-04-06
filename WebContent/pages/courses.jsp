<%@ page import="java.util.List" %>
<%@ page import="com.school.model.User" %>
<%@ page import="com.school.model.Course" %>
<%@ page import="com.school.model.ClassRoom" %>
<%@ page import="com.school.dao.CourseDAO" %>
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

CourseDAO courseDao = new CourseDAO();
UserDAO userDao = new UserDAO();
ClassRoomDAO classDao = new ClassRoomDAO();

List<Course> courses = courseDao.getAllCourses();
List<User> teachers = userDao.getUsersByRole("teacher");
List<ClassRoom> classes = classDao.getAllClasses();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Courses Management</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="navbar">
    <div class="brand">Courses</div>
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
    <h1>Courses Management</h1>
    <p>Add courses, assign them to teachers and classes, and manage all course records.</p>
</div>

<div class="container">
    <% if (request.getParameter("courseOnlySuccess") != null) { %>
        <p class="success">Course added successfully.</p>
    <% } %>
    <% if (request.getParameter("assigned") != null) { %>
        <p class="success">Course assigned successfully.</p>
    <% } %>
    <% if (request.getParameter("deletedCourse") != null) { %>
        <p class="success">Course deleted successfully.</p>
    <% } %>
    <% if (request.getParameter("error") != null) { %>
        <p class="error">Operation failed.</p>
    <% } %>

    <div class="card">
        <h2>Add Course</h2>
        <form action="<%= path %>/addCourseOnly" method="post" class="form-grid">
            <input type="text" name="name" placeholder="Course name" required>
            <button type="submit">Add Course</button>
        </form>
    </div>

    <div class="card">
        <h2>Assign Course</h2>
        <form action="<%= path %>/assignCourse" method="post" class="form-grid">
            <select name="courseId" required>
                <option value="">Select course</option>
                <% for (Course c : courses) { %>
                    <option value="<%= c.getId() %>"><%= c.getName() %></option>
                <% } %>
            </select>

            <select name="teacherId" required>
                <option value="">Select teacher</option>
                <% for (User t : teachers) { %>
                    <option value="<%= t.getId() %>"><%= t.getName() %></option>
                <% } %>
            </select>

            <select name="classId" required>
                <option value="">Select class</option>
                <% for (ClassRoom cl : classes) { %>
                    <option value="<%= cl.getId() %>"><%= cl.getName() %></option>
                <% } %>
            </select>

            <button type="submit">Assign Course</button>
        </form>
    </div>

    <div class="card">
        <h2>All Courses</h2>

        <% if (courses == null || courses.isEmpty()) { %>
            <p>No courses available.</p>
        <% } else { %>
            <table class="table">
                <tr>
                    <th>ID</th>
                    <th>Course</th>
                    <th>Teacher</th>
                    <th>Class</th>
                    <th>Action</th>
                </tr>

                <% for (Course c : courses) { %>
                <tr>
                    <td><%= c.getId() %></td>
                    <td><%= c.getName() %></td>
                    <td><%= c.getTeacherName() == null ? "-" : c.getTeacherName() %></td>
                    <td><%= c.getClassName() == null ? "-" : c.getClassName() %></td>
                    <td>
                        <form action="<%= path %>/deleteCourse" method="post" onsubmit="return confirm('Delete this course?');">
                            <input type="hidden" name="id" value="<%= c.getId() %>">
                            <button type="submit" class="danger small-btn">Delete</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
        <% } %>
    </div>
</div>

</body>
</html>