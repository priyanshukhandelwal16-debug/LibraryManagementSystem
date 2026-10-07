<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%
    request.setAttribute("pageTitle", "Member Details - Library Management System");
    request.setAttribute("pageHeading", "Member Details");
    Member member = (Member) request.getAttribute("member");
%>
<%@ include file="header.jsp" %>

<div class="panel" style="max-width:800px;">
    <div class="details-grid">
        <div class="item"><div class="k">Member ID</div><div class="v"><%= member.getMemberId() %></div></div>
        <div class="item"><div class="k">Full Name</div><div class="v"><%= member.getFullName() %></div></div>
        <div class="item"><div class="k">Email</div><div class="v"><%= member.getEmail() %></div></div>
        <div class="item"><div class="k">Phone</div><div class="v"><%= member.getPhone() != null ? member.getPhone() : "-" %></div></div>
        <div class="item"><div class="k">Gender</div><div class="v"><%= member.getGender() != null ? member.getGender() : "-" %></div></div>
        <div class="item"><div class="k">Course</div><div class="v"><%= member.getCourse() != null ? member.getCourse() : "-" %></div></div>
        <div class="item"><div class="k">Semester</div><div class="v"><%= member.getSemester() != null ? member.getSemester() : "-" %></div></div>
        <div class="item"><div class="k">Address</div><div class="v"><%= member.getAddress() != null ? member.getAddress() : "-" %></div></div>
        <div class="item"><div class="k">Registration Date</div><div class="v"><%= member.getRegistrationDate() %></div></div>
    </div>

    <div class="form-actions">
        <a href="<%= request.getContextPath() %>/EditMemberServlet?id=<%= member.getId() %>" class="btn btn-accent">Edit Member</a>
        <a href="<%= request.getContextPath() %>/MemberServlet" class="btn btn-outline">Back to Members</a>
    </div>
</div>

<%@ include file="footer.jsp" %>
