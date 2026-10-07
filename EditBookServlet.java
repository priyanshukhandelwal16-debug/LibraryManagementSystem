package com.library.controller;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class EditBookServlet extends HttpServlet {

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
        request.getRequestDispatcher("edit-book.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String isbn = trim(request.getParameter("isbn"));
        String title = trim(request.getParameter("title"));
        String author = trim(request.getParameter("author"));
        String publisher = trim(request.getParameter("publisher"));
        String category = trim(request.getParameter("category"));

        StringBuilder error = new StringBuilder();
        int year = 0, qty = 0, available = 0;

        try {
            year = Integer.parseInt(trim(request.getParameter("publicationYear")));
        } catch (NumberFormatException e) {
            error.append("Publication year must be a number. ");
        }
        try {
            qty = Integer.parseInt(trim(request.getParameter("quantity")));
        } catch (NumberFormatException e) {
            error.append("Quantity must be a number. ");
        }
        try {
            available = Integer.parseInt(trim(request.getParameter("availableQuantity")));
        } catch (NumberFormatException e) {
            error.append("Available quantity must be a number. ");
        }

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
            error.append("ISBN, Title and Author are required. ");
        }
        if (available > qty) {
            error.append("Available quantity cannot exceed total quantity. ");
        }
        if (qty < 0 || available < 0) {
            error.append("Quantities cannot be negative. ");
        }

        if (error.length() > 0) {
            Book book = bookDAO.getBookById(id);
            request.setAttribute("book", book);
            request.setAttribute("error", error.toString());
            request.getRequestDispatcher("edit-book.jsp").forward(request, response);
            return;
        }

        Book book = new Book(isbn, title, author, publisher, category, year, qty, available);
        book.setId(id);

        boolean success = bookDAO.updateBook(book);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/BookServlet?msg=updated");
        } else {
            request.setAttribute("book", book);
            request.setAttribute("error", "Could not update book.");
            request.getRequestDispatcher("edit-book.jsp").forward(request, response);
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
