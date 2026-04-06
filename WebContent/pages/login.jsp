<%@ page import="com.school.model.User" %>

<%
String path = request.getContextPath();
User user = (User) session.getAttribute("user");

if (user != null) {
    String role = user.getRole();

    if (role != null) {
        role = role.trim();

        if ("admin".equalsIgnoreCase(role)) {
            response.sendRedirect(path + "/pages/admin.jsp");
            return;
        } else if ("teacher".equalsIgnoreCase(role)) {
            response.sendRedirect(path + "/pages/teacher.jsp");
            return;
        } else if ("student".equalsIgnoreCase(role)) {
            response.sendRedirect(path + "/pages/student.jsp");
            return;
        }
    }
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - School System</title>
    <link rel="stylesheet" href="<%= path %>/css/style.css">
</head>
<body>

<div class="center-wrap">
    <div class="card login-card">
        <h1>School System Login</h1>
        <p class="muted">Sign in as admin, teacher, or student.</p>

        <% if (request.getParameter("error") != null) { %>
            <p class="error">Invalid email or password.</p>
        <% } %>

        <form action="<%= path %>/login" method="post" class="form-grid">
            <input type="email" name="email" placeholder="Email" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit">Login</button>
        </form>
    </div>
</div>

</body>
</html>