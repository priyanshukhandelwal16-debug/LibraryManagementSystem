<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Transaction" %>
<%@ page import="java.util.List" %>
<%
    request.setAttribute("pageTitle", "Dashboard - Library Management System");
    request.setAttribute("pageHeading", "Dashboard");
%>
<%@ include file="header.jsp" %>

<div class="stats-grid">
    <div class="stat-card">
        <div class="label">Total Books</div>
        <div class="value"><%= request.getAttribute("totalBooks") %></div>
    </div>
    <div class="stat-card success">
        <div class="label">Available Books</div>
        <div class="value"><%= request.getAttribute("availableBooks") %></div>
    </div>
    <div class="stat-card accent">
        <div class="label">Total Members</div>
        <div class="value"><%= request.getAttribute("totalMembers") %></div>
    </div>
    <div class="stat-card warning">
        <div class="label">Issued Books</div>
        <div class="value"><%= request.getAttribute("issuedBooks") %></div>
    </div>
    <div class="stat-card">
        <div class="label">Returned Books</div>
        <div class="value"><%= request.getAttribute("returnedBooks") %></div>
    </div>
</div>

<div class="panel">
    <h2>Recent Transactions</h2>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Txn ID</th>
                    <th>Member</th>
                    <th>Book</th>
                    <th>Issue Date</th>
                    <th>Return Date</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Transaction> recent = (List<Transaction>) request.getAttribute("recentTransactions");
                if (recent == null || recent.isEmpty()) {
            %>
                <tr><td colspan="6" class="empty-state">No transactions yet.</td></tr>
            <%
                } else {
                    for (Transaction t : recent) {
                        String badgeClass = "badge-issued";
                        if ("RETURNED".equalsIgnoreCase(t.getStatus())) badgeClass = "badge-returned";
                        else if ("OVERDUE".equalsIgnoreCase(t.getStatus())) badgeClass = "badge-overdue";
            %>
                <tr>
                    <td>#<%= t.getId() %></td>
                    <td><%= t.getMemberName() %></td>
                    <td><%= t.getBookTitle() %></td>
                    <td><%= t.getIssueDate() %></td>
                    <td><%= t.getReturnDate() != null ? t.getReturnDate().toString() : "-" %></td>
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
