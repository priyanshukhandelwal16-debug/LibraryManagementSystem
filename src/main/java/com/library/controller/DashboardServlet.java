package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class DashboardServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Keep OVERDUE status up to date every time the dashboard loads
        transactionDAO.refreshOverdueStatuses();

        int totalBooks = bookDAO.getTotalBooksCount();
        int availableBooks = bookDAO.getAvailableBooksCount();
        int totalMembers = memberDAO.getTotalMembersCount();
        int issuedBooks = transactionDAO.getIssuedCount();
        int returnedBooks = transactionDAO.getReturnedCount();
        List<Transaction> recentTransactions = transactionDAO.getRecentTransactions(10);

        request.setAttribute("totalBooks", totalBooks);
        request.setAttribute("availableBooks", availableBooks);
        request.setAttribute("totalMembers", totalMembers);
        request.setAttribute("issuedBooks", issuedBooks);
        request.setAttribute("returnedBooks", returnedBooks);
        request.setAttribute("recentTransactions", recentTransactions);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
