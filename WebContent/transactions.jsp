<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Transaction" %>
<%@ page import="java.util.List" %>
<%
    request.setAttribute("pageTitle", "Transactions - Library Management System");
    request.setAttribute("pageHeading", "Transaction History");
    String currentFilter = (String) request.getAttribute("currentFilter");
    if (currentFilter == null) currentFilter = "ALL";
%>
<%@ include file="header.jsp" %>

<div class="panel">
    <div class="filter-tabs">
        <a href="<%= request.getContextPath() %>/TransactionServlet?status=ALL" class="<%= currentFilter.equals("ALL") ? "active" : "" %>">All</a>
        <a href="<%= request.getContextPath() %>/TransactionServlet?status=ISSUED" class="<%= currentFilter.equals("ISSUED") ? "active" : "" %>">Issued</a>
        <a href="<%= request.getContextPath() %>/TransactionServlet?status=RETURNED" class="<%= currentFilter.equals("RETURNED") ? "active" : "" %>">Returned</a>
        <a href="<%= request.getContextPath() %>/TransactionServlet?status=OVERDUE" class="<%= currentFilter.equals("OVERDUE") ? "active" : "" %>">Overdue</a>
    </div>

    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Txn ID</th>
                    <th>Member</th>
                    <th>Book</th>
                    <th>Issue Date</th>
                    <th>Due Date</th>
                    <th>Return Date</th>
                    <th>Fine (Rs.)</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
                if (transactions == null || transactions.isEmpty()) {
            %>
                <tr><td colspan="8" class="empty-state">No transactions found for this filter.</td></tr>
            <%
                } else {
                    for (Transaction t : transactions) {
                        String badgeClass = "badge-issued";
                        if ("RETURNED".equalsIgnoreCase(t.getStatus())) badgeClass = "badge-returned";
                        else if ("OVERDUE".equalsIgnoreCase(t.getStatus())) badgeClass = "badge-overdue";
            %>
                <tr>
                    <td>#<%= t.getId() %></td>
                    <td><%= t.getMemberName() %></td>
                    <td><%= t.getBookTitle() %></td>
                    <td><%= t.getIssueDate() %></td>
                    <td><%= t.getDueDate() %></td>
                    <td><%= t.getReturnDate() != null ? t.getReturnDate().toString() : "-" %></td>
                    <td><%= t.getFine() %></td>
                    <td><span class="badge <%= badgeClass %>"><%= t.getStatus() %></span></td>
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
