<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("pageTitle", "Add Member - Library Management System");
    request.setAttribute("pageHeading", "Add New Member");
%>
<%@ include file="header.jsp" %>

<div class="panel" style="max-width:900px;">
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/AddMemberServlet" method="post" class="validate-form">
        <div class="js-validation-error alert alert-error" style="display:none;"></div>

        <div class="form-grid">
            <div class="form-group">
                <label>Member ID (auto-generated)</label>
                <input type="text" value="<%= request.getAttribute("nextMemberId") %>" disabled>
            </div>
            <div class="form-group">
                <label for="fullName">Full Name *</label>
                <input type="text" id="fullName" name="fullName" data-label="Full Name" required>
            </div>
            <div class="form-group">
                <label for="email">Email *</label>
                <input type="email" id="email" name="email" data-label="Email" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone (10 digits)</label>
                <input type="text" id="phone" name="phone" pattern="[0-9]{10}" maxlength="10">
            </div>
            <div class="form-group">
                <label for="gender">Gender</label>
                <select id="gender" name="gender">
                    <option value="">-- Select --</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Other">Other</option>
                </select>
            </div>
            <div class="form-group">
                <label for="course">Course</label>
                <input type="text" id="course" name="course" placeholder="e.g. B.Tech CSE">
            </div>
            <div class="form-group">
                <label for="semester">Semester</label>
                <input type="text" id="semester" name="semester" placeholder="e.g. 5">
            </div>
            <div class="form-group full">
                <label for="address">Address</label>
                <textarea id="address" name="address"></textarea>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Member</button>
            <a href="<%= request.getContextPath() %>/MemberServlet" class="btn btn-outline">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="footer.jsp" %>
