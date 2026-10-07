<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="java.util.List" %>
<%
    request.setAttribute("pageTitle", "Members - Library Management System");
    request.setAttribute("pageHeading", "Member Management");
    String msg = request.getParameter("msg");
%>
<%@ include file="header.jsp" %>

<% if ("added".equals(msg)) { %>
    <div class="alert alert-success">Member added successfully.</div>
<% } else if ("updated".equals(msg)) { %>
    <div class="alert alert-success">Member updated successfully.</div>
<% } else if ("deleted".equals(msg)) { %>
    <div class="alert alert-success">Member deleted successfully.</div>
<% } else if ("delete_failed".equals(msg)) { %>
    <div class="alert alert-error">Could not delete this member (they may have active transactions).</div>
<% } %>

<div class="panel">
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; flex-wrap:wrap; gap:12px;">
        <div style="flex:1; min-width:260px;">
            <input type="text" id="liveSearchInput" placeholder="Quick filter this page (name, email, ID)...">
        </div>
        <a href="<%= request.getContextPath() %>/AddMemberServlet" class="btn btn-primary">+ Add Member</a>
    </div>

    <div class="table-wrap">
        <table class="searchable">
            <thead>
                <tr>
                    <th>Member ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Course</th>
                    <th>Semester</th>
                    <th>Registered On</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Member> members = (List<Member>) request.getAttribute("members");
                if (members == null || members.isEmpty()) {
            %>
                <tr><td colspan="8" class="empty-state">No members found.</td></tr>
            <%
                } else {
                    for (Member m : members) {
            %>
                <tr>
                    <td><%= m.getMemberId() %></td>
                    <td><%= m.getFullName() %></td>
                    <td><%= m.getEmail() %></td>
                    <td><%= m.getPhone() != null ? m.getPhone() : "-" %></td>
                    <td><%= m.getCourse() != null ? m.getCourse() : "-" %></td>
                    <td><%= m.getSemester() != null ? m.getSemester() : "-" %></td>
                    <td><%= m.getRegistrationDate() %></td>
                    <td>
                        <div class="actions-cell">
                            <a class="btn btn-outline btn-sm" href="<%= request.getContextPath() %>/MemberDetailsServlet?id=<%= m.getId() %>">View</a>
                            <a class="btn btn-accent btn-sm" href="<%= request.getContextPath() %>/EditMemberServlet?id=<%= m.getId() %>">Edit</a>
                            <a class="btn btn-danger btn-sm confirm-delete" data-confirm-message="Delete this member permanently?" href="<%= request.getContextPath() %>/DeleteMemberServlet?id=<%= m.getId() %>">Delete</a>
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
