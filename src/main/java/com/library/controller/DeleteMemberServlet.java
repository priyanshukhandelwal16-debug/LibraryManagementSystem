package com.library.controller;

import com.library.dao.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class DeleteMemberServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = memberDAO.deleteMember(id);

        String msg = success ? "deleted" : "delete_failed";
        response.sendRedirect(request.getContextPath() + "/MemberServlet?msg=" + msg);
    }
}
