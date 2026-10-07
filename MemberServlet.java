package com.library.controller;

import com.library.dao.MemberDAO;
import com.library.model.Member;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class MemberServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Member> members = memberDAO.getAllMembers();
        request.setAttribute("members", members);
        request.getRequestDispatcher("members.jsp").forward(request, response);
    }
}
