package com.library.controller;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class BookDetailsServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Book book = bookDAO.getBookById(id);

        if (book == null) {
            response.sendRedirect(request.getContextPath() + "/BookServlet");
            return;
        }

        request.setAttribute("book", book);
        request.getRequestDispatcher("book-details.jsp").forward(request, response);
    }
}
