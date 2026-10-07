<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("pageTitle", "Add Book - Library Management System");
    request.setAttribute("pageHeading", "Add New Book");
%>
<%@ include file="header.jsp" %>

<div class="panel" style="max-width:900px;">
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/AddBookServlet" method="post" class="validate-form">
        <div class="js-validation-error alert alert-error" style="display:none;"></div>

        <div class="form-grid">
            <div class="form-group">
                <label for="isbn">ISBN *</label>
                <input type="text" id="isbn" name="isbn" data-label="ISBN" required>
            </div>
            <div class="form-group">
                <label for="title">Book Title *</label>
                <input type="text" id="title" name="title" data-label="Title" required>
            </div>
            <div class="form-group">
                <label for="author">Author *</label>
                <input type="text" id="author" name="author" data-label="Author" required>
            </div>
            <div class="form-group">
                <label for="publisher">Publisher</label>
                <input type="text" id="publisher" name="publisher">
            </div>
            <div class="form-group">
                <label for="category">Category</label>
                <input type="text" id="category" name="category" placeholder="e.g. Programming, Fiction">
            </div>
            <div class="form-group">
                <label for="publicationYear">Publication Year *</label>
                <input type="number" id="publicationYear" name="publicationYear" data-label="Publication Year" min="1000" max="2100" required>
            </div>
            <div class="form-group">
                <label for="quantity">Quantity *</label>
                <input type="number" id="quantity" name="quantity" data-label="Quantity" min="0" required>
            </div>
        </div>

        <p style="color:#6b7280; font-size:12.5px; margin-top:14px;">Note: Available quantity is automatically set equal to the total quantity when a book is first added.</p>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Book</button>
            <a href="<%= request.getContextPath() %>/BookServlet" class="btn btn-outline">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="footer.jsp" %>
