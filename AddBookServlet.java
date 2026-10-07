package com.library.controller;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class AddBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("add-book.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String isbn = trim(request.getParameter("isbn"));
        String title = trim(request.getParameter("title"));
        String author = trim(request.getParameter("author"));
        String publisher = trim(request.getParameter("publisher"));
        String category = trim(request.getParameter("category"));
        String yearStr = trim(request.getParameter("publicationYear"));
        String qtyStr = trim(request.getParameter("quantity"));

        StringBuilder error = new StringBuilder();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
            error.append("ISBN, Title and Author are required. ");
        }

        int year = 0, qty = 0;
        try {
            year = Integer.parseInt(yearStr);
            if (year < 1000 || year > 2100) error.append("Enter a valid publication year. ");
        } catch (NumberFormatException e) {
            error.append("Publication year must be a number. ");
        }

        try {
            qty = Integer.parseInt(qtyStr);
            if (qty < 0) error.append("Quantity cannot be negative. ");
        } catch (NumberFormatException e) {
            error.append("Quantity must be a number. ");
        }

        if (error.length() > 0) {
            request.setAttribute("error", error.toString());
            request.getRequestDispatcher("add-book.jsp").forward(request, response);
            return;
        }

        Book book = new Book(isbn, title, author, publisher, category, year, qty, qty);
        boolean success = bookDAO.addBook(book);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/BookServlet?msg=added");
        } else {
            request.setAttribute("error", "Could not add book. The ISBN may already exist.");
            request.getRequestDispatcher("add-book.jsp").forward(request, response);
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
