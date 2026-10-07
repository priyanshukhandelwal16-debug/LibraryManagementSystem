<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Book" %>
<%@ page import="java.util.List" %>
<%
    request.setAttribute("pageTitle", "Books - Library Management System");
    request.setAttribute("pageHeading", "Book Management");
    String msg = request.getParameter("msg");
%>
<%@ include file="header.jsp" %>

<% if ("added".equals(msg)) { %>
    <div class="alert alert-success">Book added successfully.</div>
<% } else if ("updated".equals(msg)) { %>
    <div class="alert alert-success">Book updated successfully.</div>
<% } else if ("deleted".equals(msg)) { %>
    <div class="alert alert-success">Book deleted successfully.</div>
<% } else if ("delete_failed".equals(msg)) { %>
    <div class="alert alert-error">Could not delete this book (it may have active transactions).</div>
<% } %>

<div class="panel">
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; flex-wrap:wrap; gap:12px;">
        <form action="<%= request.getContextPath() %>/SearchBookServlet" method="get" class="search-bar" style="margin-bottom:0; flex:1; min-width:260px;">
            <input type="text" name="keyword" placeholder="Search by ID, ISBN, title, author or category" value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>">
            <button type="submit" class="btn btn-outline">Search</button>
        </form>
        <a href="<%= request.getContextPath() %>/AddBookServlet" class="btn btn-primary">+ Add Book</a>
    </div>

    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Book ID</th>
                    <th>ISBN</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Publisher</th>
                    <th>Category</th>
                    <th>Year</th>
                    <th>Qty</th>
                    <th>Available</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Book> books = (List<Book>) request.getAttribute("books");
                if (books == null || books.isEmpty()) {
            %>
                <tr><td colspan="10" class="empty-state">No books found.</td></tr>
            <%
                } else {
                    for (Book b : books) {
            %>
                <tr>
                    <td>#<%= b.getId() %></td>
                    <td><%= b.getIsbn() %></td>
                    <td><%= b.getTitle() %></td>
                    <td><%= b.getAuthor() %></td>
                    <td><%= b.getPublisher() != null ? b.getPublisher() : "-" %></td>
                    <td><%= b.getCategory() != null ? b.getCategory() : "-" %></td>
                    <td><%= b.getPublicationYear() %></td>
                    <td><%= b.getQuantity() %></td>
                    <td><%= b.getAvailableQuantity() %></td>
                    <td>
                        <div class="actions-cell">
                            <a class="btn btn-outline btn-sm" href="<%= request.getContextPath() %>/BookDetailsServlet?id=<%= b.getId() %>">View</a>
                            <a class="btn btn-accent btn-sm" href="<%= request.getContextPath() %>/EditBookServlet?id=<%= b.getId() %>">Edit</a>
                            <a class="btn btn-danger btn-sm confirm-delete" data-confirm-message="Delete this book permanently?" href="<%= request.getContextPath() %>/DeleteBookServlet?id=<%= b.getId() %>">Delete</a>
                        </div>
                    </td>
                </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="footer.jsp" %>
