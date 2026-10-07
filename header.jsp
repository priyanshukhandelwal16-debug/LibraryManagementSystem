<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String currentPage = request.getServletPath();
    if (currentPage == null) currentPage = "";
    Object titleAttr = request.getAttribute("pageTitle");
    Object headingAttr = request.getAttribute("pageHeading");
    String pageTitle = (titleAttr != null) ? titleAttr.toString() : "Library Management System";
    String pageHeading = (headingAttr != null) ? headingAttr.toString() : "";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="app-shell">
    <aside class="sidebar">
        <div class="brand">
            <div class="logo">&#128218;</div>
            <h2>LibraryMS</h2>
        </div>
        <nav>
            <a href="<%= request.getContextPath() %>/DashboardServlet" class='<%= currentPage.contains("Dashboard") ? "active" : "" %>'>Dashboard</a>
            <a href="<%= request.getContextPath() %>/BookServlet" class='<%= (currentPage.contains("Book") && !currentPage.contains("Issue")) ? "active" : "" %>'>Books</a>
            <a href="<%= request.getContextPath() %>/MemberServlet" class='<%= currentPage.contains("Member") ? "active" : "" %>'>Members</a>
            <a href="<%= request.getContextPath() %>/IssueBookServlet" class='<%= currentPage.contains("IssueBook") ? "active" : "" %>'>Issue Book</a>
            <a href="<%= request.getContextPath() %>/ReturnBookServlet" class='<%= currentPage.contains("ReturnBook") ? "active" : "" %>'>Return Book</a>
            <a href="<%= request.getContextPath() %>/TransactionServlet" class='<%= currentPage.contains("Transaction") ? "active" : "" %>'>Transactions</a>
            <a href="<%= request.getContextPath() %>/SearchServlet" class='<%= currentPage.contains("Search") ? "active" : "" %>'>Search</a>
            <a href="<%= request.getContextPath() %>/LogoutServlet">Logout</a>
        </nav>
    </aside>

    <main class="main-content">
        <div class="topbar">
            <h1><%= pageHeading %></h1>
            <div class="user-chip">Logged in as <b><%= session.getAttribute("admin") %></b></div>
        </div>
