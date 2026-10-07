package com.library.controller;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class SearchBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        List<Book> books;

        if (keyword == null || keyword.trim().isEmpty()) {
            books = bookDAO.getAllBooks();
        } else {
            books = bookDAO.searchBooks(keyword.trim());
        }

        request.setAttribute("books", books);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("books.jsp").forward(request, response);
    }
}
