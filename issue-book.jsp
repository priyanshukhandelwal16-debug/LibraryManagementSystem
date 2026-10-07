<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="com.library.model.Book" %>
<%@ page import="java.util.List" %>
<%
    request.setAttribute("pageTitle", "Issue Book - Library Management System");
    request.setAttribute("pageHeading", "Issue a Book");
    String msg = request.getParameter("msg");
%>
<%@ include file="header.jsp" %>

<% if ("issued".equals(msg)) { %>
    <div class="alert alert-success">Book issued successfully.</div>
<% } %>
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("error") %></div>
<% } %>

<div class="panel" style="max-width:800px;">
    <form action="<%= request.getContextPath() %>/IssueBookServlet" method="post" class="validate-form">
        <div class="js-validation-error alert alert-error" style="display:none;"></div>

        <div class="form-grid">
            <div class="form-group">
                <label for="memberId">Select Member *</label>
                <select id="memberId" name="memberId" data-label="Member" required>
                    <option value="">-- Choose a member --</option>
                    <%
                        List<Member> members = (List<Member>) request.getAttribute("members");
                        if (members != null) {
                            for (Member m : members) {
                    %>
                        <option value="<%= m.getId() %>"><%= m.getMemberId() %> - <%= m.getFullName() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="bookId">Select Book *</label>
                <select id="bookId" name="bookId" data-label="Book" required>
                    <option value="">-- Choose an available book --</option>
                    <%
                        List<Book> books = (List<Book>) request.getAttribute("books");
                        if (books == null || books.isEmpty()) {
                    %>
                        <option value="" disabled>No books currently available</option>
                    <%
                        } else {
                            for (Book b : books) {
                    %>
                        <option value="<%= b.getId() %>"><%= b.getTitle() %> (Available: <%= b.getAvailableQuantity() %>)</option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="issueDate">Issue Date *</label>
                <input type="date" id="issueDate" name="issueDate" value="<%= request.getAttribute("today") %>" required>
            </div>
            <div class="form-group">
                <label for="dueDate">Due Date *</label>
                <input type="date" id="dueDate" name="dueDate" value="<%= request.getAttribute("defaultDueDate") %>" required>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Issue Book</button>
            <a href="<%= request.getContextPath() %>/DashboardServlet" class="btn btn-outline">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="footer.jsp" %>
