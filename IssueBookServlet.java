package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.util.FineConfig;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class IssueBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("members", memberDAO.getAllMembers());
        request.setAttribute("books", bookDAO.getAvailableBooks());
        request.setAttribute("today", LocalDate.now().toString());
        request.setAttribute("defaultDueDate", LocalDate.now().plusDays(FineConfig.DEFAULT_LOAN_DAYS).toString());
        request.getRequestDispatcher("issue-book.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String memberIdStr = request.getParameter("memberId");
        String bookIdStr = request.getParameter("bookId");
        String issueDateStr = request.getParameter("issueDate");
        String dueDateStr = request.getParameter("dueDate");

        String error = null;

        if (isBlank(memberIdStr) || isBlank(bookIdStr) || isBlank(issueDateStr) || isBlank(dueDateStr)) {
            error = "All fields are required.";
        }

        Date issueDate = null, dueDate = null;
        if (error == null) {
            try {
                issueDate = Date.valueOf(issueDateStr);
                dueDate = Date.valueOf(dueDateStr);
                if (dueDate.before(issueDate)) {
                    error = "Due date cannot be before issue date.";
                }
            } catch (IllegalArgumentException e) {
                error = "Invalid date format.";
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("members", memberDAO.getAllMembers());
            request.setAttribute("books", bookDAO.getAvailableBooks());
            request.getRequestDispatcher("issue-book.jsp").forward(request, response);
            return;
        }

        int memberId = Integer.parseInt(memberIdStr);
        int bookId = Integer.parseInt(bookIdStr);

        String result = transactionDAO.issueBook(memberId, bookId, issueDate, dueDate);

        if ("SUCCESS".equals(result)) {
            response.sendRedirect(request.getContextPath() + "/IssueBookServlet?msg=issued");
        } else {
            String msg = "UNAVAILABLE".equals(result)
                    ? "Book is currently unavailable."
                    : "Could not issue the book. Please try again.";
            request.setAttribute("error", msg);
            request.setAttribute("members", memberDAO.getAllMembers());
            request.setAttribute("books", bookDAO.getAvailableBooks());
            request.getRequestDispatcher("issue-book.jsp").forward(request, response);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
