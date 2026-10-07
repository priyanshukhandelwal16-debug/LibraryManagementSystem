package com.library.controller;

import com.library.dao.BookDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class DeleteBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = bookDAO.deleteBook(id);

        String msg = success ? "deleted" : "delete_failed";
        response.sendRedirect(request.getContextPath() + "/BookServlet?msg=" + msg);
    }
}
