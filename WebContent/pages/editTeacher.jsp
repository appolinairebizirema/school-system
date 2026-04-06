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

int teacherId = Integer.parseInt(request.getParameter("id"));

UserDAO userDao = new UserDAO();
CourseDAO courseDao = new CourseDAO();
ClassRoomDAO classDao = new ClassRoomDAO();

User teacher = userDao.getUserById(teacherId);

if (teacher == null || !"teacher".equalsIgnoreCase(teacher.getRole())) {
    response.sendRedirect(path + "/pages/teachers.jsp");
    return;
}

List<Course> allCourses = courseDao.getAllCourses();
List<Course> teacherCourses = courseDao.getCoursesByTeacher(teacherId);
List<ClassRoom> classes = classDao.getAllClasses();

Integer selectedCourseId = null;
Integer selectedClassId = null;

if (teacherCourses != null && !teacherCourses.isEmpty()) {
    selectedCourseId = teacherCourses.get(0).getId();
    selectedClassId = teacherCourses.get(0).getClassId();
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Teacher</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="navbar">
    <div class="brand">Edit Teacher</div>
    <div class="nav-links">
        <a href="<%= path %>/pages/admin.jsp">Dashboard</a>
        <a href="<%= path %>/pages/teachers.jsp">Teachers</a>
        <a href="<%= path %>/logout">Logout</a>
    </div>
</div>

<div class="page-header">
    <h1>Edit Teacher Assignment</h1>
    <p>Update course and class for this teacher.</p>
</div>

<div class="container">
    <div class="card">
        <form action="<%= path %>/updateTeacherAssignment" method="post" class="form-grid">
            <input type="hidden" name="teacherId" value="<%= teacher.getId() %>">

            <input type="text" value="<%= teacher.getName() %>" readonly>
            <input type="text" value="<%= teacher.getEmail() %>" readonly>

            <select name="courseId" required>
                <option value="">Select course</option>
                <% for (Course c : allCourses) { %>
                    <option value="<%= c.getId() %>" <%= (selectedCourseId != null && selectedCourseId == c.getId()) ? "selected" : "" %>>
                        <%= c.getName() %>
                    </option>
                <% } %>
            </select>

            <select name="classId" required>
                <option value="">Select class</option>
                <% for (ClassRoom cl : classes) { %>
                    <option value="<%= cl.getId() %>" <%= (selectedClassId != null && selectedClassId == cl.getId()) ? "selected" : "" %>>
                        <%= cl.getName() %>
                    </option>
                <% } %>
            </select>

            <button type="submit">Update Teacher Assignment</button>
        </form>
    </div>
</div>

</body>
</html>