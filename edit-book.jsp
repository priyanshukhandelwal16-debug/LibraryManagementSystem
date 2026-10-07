<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Book" %>
<%
    request.setAttribute("pageTitle", "Edit Book - Library Management System");
    request.setAttribute("pageHeading", "Edit Book");
    Book book = (Book) request.getAttribute("book");
%>
<%@ include file="header.jsp" %>

<div class="panel" style="max-width:900px;">
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/EditBookServlet" method="post" class="validate-form">
        <div class="js-validation-error alert alert-error" style="display:none;"></div>
        <input type="hidden" name="id" value="<%= book.getId() %>">

        <div class="form-grid">
            <div class="form-group">
                <label for="isbn">ISBN *</label>
                <input type="text" id="isbn" name="isbn" data-label="ISBN" value="<%= book.getIsbn() %>" required>
            </div>
            <div class="form-group">
                <label for="title">Book Title *</label>
                <input type="text" id="title" name="title" data-label="Title" value="<%= book.getTitle() %>" required>
            </div>
            <div class="form-group">
                <label for="author">Author *</label>
                <input type="text" id="author" name="author" data-label="Author" value="<%= book.getAuthor() %>" required>
            </div>
            <div class="form-group">
                <label for="publisher">Publisher</label>
                <input type="text" id="publisher" name="publisher" value="<%= book.getPublisher() != null ? book.getPublisher() : "" %>">
            </div>
            <div class="form-group">
                <label for="category">Category</label>
                <input type="text" id="category" name="category" value="<%= book.getCategory() != null ? book.getCategory() : "" %>">
            </div>
            <div class="form-group">
                <label for="publicationYear">Publication Year *</label>
                <input type="number" id="publicationYear" name="publicationYear" min="1000" max="2100" value="<%= book.getPublicationYear() %>" required>
            </div>
            <div class="form-group">
                <label for="quantity">Total Quantity *</label>
                <input type="number" id="quantity" name="quantity" min="0" value="<%= book.getQuantity() %>" required>
            </div>
            <div class="form-group">
                <label for="availableQuantity">Available Quantity *</label>
                <input type="number" id="availableQuantity" name="availableQuantity" min="0" value="<%= book.getAvailableQuantity() %>" required>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Update Book</button>
            <a href="<%= request.getContextPath() %>/BookServlet" class="btn btn-outline">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="footer.jsp" %>
