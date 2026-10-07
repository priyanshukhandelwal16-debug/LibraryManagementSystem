<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Book" %>
<%
    request.setAttribute("pageTitle", "Book Details - Library Management System");
    request.setAttribute("pageHeading", "Book Details");
    Book book = (Book) request.getAttribute("book");
%>
<%@ include file="header.jsp" %>

<div class="panel" style="max-width:800px;">
    <div class="details-grid">
        <div class="item"><div class="k">Book ID</div><div class="v">#<%= book.getId() %></div></div>
        <div class="item"><div class="k">ISBN</div><div class="v"><%= book.getIsbn() %></div></div>
        <div class="item"><div class="k">Title</div><div class="v"><%= book.getTitle() %></div></div>
        <div class="item"><div class="k">Author</div><div class="v"><%= book.getAuthor() %></div></div>
        <div class="item"><div class="k">Publisher</div><div class="v"><%= book.getPublisher() != null ? book.getPublisher() : "-" %></div></div>
        <div class="item"><div class="k">Category</div><div class="v"><%= book.getCategory() != null ? book.getCategory() : "-" %></div></div>
        <div class="item"><div class="k">Publication Year</div><div class="v"><%= book.getPublicationYear() %></div></div>
        <div class="item"><div class="k">Total Quantity</div><div class="v"><%= book.getQuantity() %></div></div>
        <div class="item"><div class="k">Available Quantity</div><div class="v"><%= book.getAvailableQuantity() %></div></div>
        <div class="item"><div class="k">Added On</div><div class="v"><%= book.getCreatedAt() %></div></div>
    </div>

    <div class="form-actions">
        <a href="<%= request.getContextPath() %>/EditBookServlet?id=<%= book.getId() %>" class="btn btn-accent">Edit Book</a>
        <a href="<%= request.getContextPath() %>/BookServlet" class="btn btn-outline">Back to Books</a>
    </div>
</div>

<%@ include file="footer.jsp" %>
