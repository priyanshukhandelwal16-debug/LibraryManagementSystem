<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Book" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="com.library.model.Transaction" %>
<%@ page import="java.util.List" %>
<%
    request.setAttribute("pageTitle", "Search - Library Management System");
    request.setAttribute("pageHeading", "Search");
    String type = (String) request.getAttribute("type");
    if (type == null) type = "books";
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
%>
<%@ include file="header.jsp" %>

<div class="panel">
    <form action="<%= request.getContextPath() %>/SearchServlet" method="get" style="display:flex; gap:10px; flex-wrap:wrap; margin-bottom:20px;">
        <select name="type" style="max-width:220px;">
            <option value="books" <%= "books".equals(type) ? "selected" : "" %>>Books (title, author, ISBN, category)</option>
            <option value="members" <%= "members".equals(type) ? "selected" : "" %>>Members (ID, name, email)</option>
            <option value="transactions" <%= "transactions".equals(type) ? "selected" : "" %>>Transactions (member, book, status)</option>
        </select>
        <input type="text" name="keyword" placeholder="Enter search keyword..." value="<%= keyword %>" style="flex:1; min-width:220px;">
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <%
        if ("books".equals(type)) {
            List<Book> books = (List<Book>) request.getAttribute("books");
    %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr><th>Book ID</th><th>ISBN</th><th>Title</th><th>Author</th><th>Category</th><th>Available</th></tr>
            </thead>
            <tbody>
            <% if (books == null || books.isEmpty()) { %>
                <tr><td colspan="6" class="empty-state">No results. Enter a keyword above to search books.</td></tr>
            <% } else {
                for (Book b : books) {
            %>
                <tr>
                    <td>#<%= b.getId() %></td>
                    <td><%= b.getIsbn() %></td>
                    <td><%= b.getTitle() %></td>
                    <td><%= b.getAuthor() %></td>
                    <td><%= b.getCategory() != null ? b.getCategory() : "-" %></td>
                    <td><%= b.getAvailableQuantity() %></td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
    <%
        } else if ("members".equals(type)) {
            List<Member> members = (List<Member>) request.getAttribute("members");
    %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr><th>Member ID</th><th>Name</th><th>Email</th><th>Course</th></tr>
            </thead>
            <tbody>
            <% if (members == null || members.isEmpty()) { %>
                <tr><td colspan="4" class="empty-state">No results. Enter a keyword above to search members.</td></tr>
            <% } else {
                for (Member m : members) {
            %>
                <tr>
                    <td><%= m.getMemberId() %></td>
                    <td><%= m.getFullName() %></td>
                    <td><%= m.getEmail() %></td>
                    <td><%= m.getCourse() != null ? m.getCourse() : "-" %></td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
    <%
        } else {
            List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
    %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr><th>Txn ID</th><th>Member</th><th>Book</th><th>Status</th></tr>
            </thead>
            <tbody>
            <% if (transactions == null || transactions.isEmpty()) { %>
                <tr><td colspan="4" class="empty-state">No results. Enter a keyword above to search transactions.</td></tr>
            <% } else {
                for (Transaction t : transactions) {
            %>
                <tr>
                    <td>#<%= t.getId() %></td>
                    <td><%= t.getMemberName() %></td>
                    <td><%= t.getBookTitle() %></td>
                    <td><%= t.getStatus() %></td>
                </tr>
            <% } } %>
            </tbody>
        </table>
    </div>
    <% } %>
</div>

<%@ include file="footer.jsp" %>
