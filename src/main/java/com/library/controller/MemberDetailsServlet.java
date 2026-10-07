package com.library.controller;

import com.library.dao.MemberDAO;
import com.library.model.Member;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class MemberDetailsServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Member member = memberDAO.getMemberById(id);

        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/MemberServlet");
            return;
        }

        request.setAttribute("member", member);
        request.getRequestDispatcher("member-details.jsp").forward(request, response);
    }
}
