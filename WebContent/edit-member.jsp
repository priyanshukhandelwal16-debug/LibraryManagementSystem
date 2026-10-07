<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%
    request.setAttribute("pageTitle", "Edit Member - Library Management System");
    request.setAttribute("pageHeading", "Edit Member");
    Member member = (Member) request.getAttribute("member");
%>
<%@ include file="header.jsp" %>

<div class="panel" style="max-width:900px;">
    <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/EditMemberServlet" method="post" class="validate-form">
        <div class="js-validation-error alert alert-error" style="display:none;"></div>
        <input type="hidden" name="id" value="<%= member.getId() %>">

        <div class="form-grid">
            <div class="form-group">
                <label>Member ID</label>
                <input type="text" value="<%= member.getMemberId() %>" disabled>
            </div>
            <div class="form-group">
                <label for="fullName">Full Name *</label>
                <input type="text" id="fullName" name="fullName" data-label="Full Name" value="<%= member.getFullName() %>" required>
            </div>
            <div class="form-group">
                <label for="email">Email *</label>
                <input type="email" id="email" name="email" data-label="Email" value="<%= member.getEmail() %>" required>
            </div>
            <div class="form-group">
                <label for="phone">Phone (10 digits)</label>
                <input type="text" id="phone" name="phone" pattern="[0-9]{10}" maxlength="10" value="<%= member.getPhone() != null ? member.getPhone() : "" %>">
            </div>
            <div class="form-group">
                <label for="gender">Gender</label>
                <select id="gender" name="gender">
                    <option value="">-- Select --</option>
                    <option value="Male" <%= "Male".equals(member.getGender()) ? "selected" : "" %>>Male</option>
                    <option value="Female" <%= "Female".equals(member.getGender()) ? "selected" : "" %>>Female</option>
                    <option value="Other" <%= "Other".equals(member.getGender()) ? "selected" : "" %>>Other</option>
                </select>
            </div>
            <div class="form-group">
                <label for="course">Course</label>
                <input type="text" id="course" name="course" value="<%= member.getCourse() != null ? member.getCourse() : "" %>">
            </div>
            <div class="form-group">
                <label for="semester">Semester</label>
                <input type="text" id="semester" name="semester" value="<%= member.getSemester() != null ? member.getSemester() : "" %>">
            </div>
            <div class="form-group full">
                <label for="address">Address</label>
                <textarea id="address" name="address"><%= member.getAddress() != null ? member.getAddress() : "" %></textarea>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Update Member</button>
            <a href="<%= request.getContextPath() %>/MemberServlet" class="btn btn-outline">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="footer.jsp" %>
