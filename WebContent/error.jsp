<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error - Library Management System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="login-wrapper">
        <div class="login-card" style="text-align:center;">
            <h1>Something went wrong</h1>
            <p class="sub">The page you requested could not be found, or an unexpected error occurred.</p>
            <a href="<%= request.getContextPath() %>/DashboardServlet" class="btn btn-primary">Go to Dashboard</a>
        </div>
    </div>
</body>
</html>
