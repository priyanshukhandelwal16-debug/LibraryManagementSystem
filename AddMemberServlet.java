package com.library.controller;

import com.library.dao.MemberDAO;
import com.library.model.Member;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AddMemberServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("nextMemberId", memberDAO.generateNextMemberId());
        request.getRequestDispatcher("add-member.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = trim(request.getParameter("fullName"));
        String email = trim(request.getParameter("email"));
        String phone = trim(request.getParameter("phone"));
        String gender = trim(request.getParameter("gender"));
        String course = trim(request.getParameter("course"));
        String semester = trim(request.getParameter("semester"));
        String address = trim(request.getParameter("address"));

        StringBuilder error = new StringBuilder();

        if (fullName.isEmpty()) error.append("Full name is required. ");
        if (email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) error.append("A valid email is required. ");
        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) error.append("Phone number must be 10 digits. ");

        if (error.length() > 0) {
            request.setAttribute("error", error.toString());
            request.setAttribute("nextMemberId", memberDAO.generateNextMemberId());
            request.getRequestDispatcher("add-member.jsp").forward(request, response);
            return;
        }

        Member member = new Member();
        member.setMemberId(memberDAO.generateNextMemberId());
        member.setFullName(fullName);
        member.setEmail(email);
        member.setPhone(phone);
        member.setGender(gender);
        member.setCourse(course);
        member.setSemester(semester);
        member.setAddress(address);
        member.setRegistrationDate(Date.valueOf(LocalDate.now()));

        boolean success = memberDAO.addMember(member);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/MemberServlet?msg=added");
        } else {
            request.setAttribute("error", "Could not add member. The email may already be registered.");
            request.setAttribute("nextMemberId", memberDAO.generateNextMemberId());
            request.getRequestDispatcher("add-member.jsp").forward(request, response);
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
