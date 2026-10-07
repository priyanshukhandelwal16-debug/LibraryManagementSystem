package com.library.controller;

import com.library.dao.TransactionDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ReturnBookServlet extends HttpServlet {

    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        transactionDAO.refreshOverdueStatuses();
        request.setAttribute("transactions", transactionDAO.getActiveTransactions());
        request.getRequestDispatcher("return-book.jsp").forward(request, response);
    }

    /** Handles the "Return" button action for a specific transaction id. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int transactionId = Integer.parseInt(request.getParameter("transactionId"));
        String result = transactionDAO.returnBook(transactionId);

        String msg;
        switch (result) {
            case "SUCCESS": msg = "returned"; break;
            case "ALREADY_RETURNED": msg = "already_returned"; break;
            case "NOT_FOUND": msg = "not_found"; break;
            default: msg = "error";
        }
        response.sendRedirect(request.getContextPath() + "/ReturnBookServlet?msg=" + msg);
    }
}
