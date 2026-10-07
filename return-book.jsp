<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Transaction" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%
    request.setAttribute("pageTitle", "Return Book - Library Management System");
    request.setAttribute("pageHeading", "Return a Book");
    String msg = request.getParameter("msg");
%>
<%@ include file="header.jsp" %>

<% if ("returned".equals(msg)) { %>
    <div class="alert alert-success">Book returned successfully. Fine (if any) has been recorded.</div>
<% } else if ("already_returned".equals(msg)) { %>
    <div class="alert alert-error">This book has already been returned.</div>
<% } else if ("not_found".equals(msg)) { %>
    <div class="alert alert-error">Transaction not found.</div>
<% } else if ("error".equals(msg)) { %>
    <div class="alert alert-error">Something went wrong. Please try again.</div>
<% } %>

<div class="panel">
    <h2>Currently Issued Books</h2>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Txn ID</th>
                    <th>Member</th>
                    <th>Book</th>
                    <th>Issue Date</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
                if (transactions == null || transactions.isEmpty()) {
            %>
                <tr><td colspan="7" class="empty-state">No books are currently issued.</td></tr>
            <%
                } else {
                    LocalDate today = LocalDate.now();
                    for (Transaction t : transactions) {
                        boolean overdue = t.getDueDate().toLocalDate().isBefore(today);
                        String badgeClass = overdue ? "badge-overdue" : "badge-issued";
                        String statusLabel = overdue ? "OVERDUE" : "ISSUED";
            %>
                <tr>
                    <td>#<%= t.getId() %></td>
                    <td><%= t.getMemberName() %></td>
                    <td><%= t.getBookTitle() %></td>
                    <td><%= t.getIssueDate() %></td>
                    <td><%= t.getDueDate() %></td>
                    <td><span class="badge <%= badgeClass %>"><%= statusLabel %></span></td>
                    <td>
                        <form action="<%= request.getContextPath() %>/ReturnBookServlet" method="post" class="confirm-return">
                            <input type="hidden" name="transactionId" value="<%= t.getId() %>">
                            <button type="submit" class="btn btn-success btn-sm">Return</button>
                        </form>
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
