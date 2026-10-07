package com.library.controller;

import com.library.dao.TransactionDAO;
import com.library.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class TransactionServlet extends HttpServlet {

    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        transactionDAO.refreshOverdueStatuses();

        String filter = request.getParameter("status"); // ALL, ISSUED, RETURNED, OVERDUE
        List<Transaction> transactions;

        if (filter == null || filter.isEmpty() || "ALL".equalsIgnoreCase(filter)) {
            transactions = transactionDAO.getAllTransactions();
            filter = "ALL";
        } else {
            transactions = transactionDAO.getTransactionsByStatus(filter.toUpperCase());
        }

        request.setAttribute("transactions", transactions);
        request.setAttribute("currentFilter", filter);
        request.getRequestDispatcher("transactions.jsp").forward(request, response);
    }
}
