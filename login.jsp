<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Library Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="login-wrapper">
        <div class="login-card">
            <div class="login-icon">&#128218;</div>
            <h1>Library Management System</h1>
            <p class="sub">Sign in to manage books, members &amp; transactions</p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error"><%= request.getAttribute("error") %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/LoginServlet" method="post" class="validate-form">
                <div class="js-validation-error alert alert-error" style="display:none;"></div>

                <div class="form-group" style="margin-bottom:16px;">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" data-label="Username" required autofocus>
                </div>
                <div class="form-group" style="margin-bottom:8px;">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" data-label="Password" required>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary" style="width:100%;">Login</button>
                </div>
            </form>

            <div class="hint-box">Default credentials &mdash; Username: <b>admin</b> &nbsp;|&nbsp; Password: <b>admin123</b></div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
