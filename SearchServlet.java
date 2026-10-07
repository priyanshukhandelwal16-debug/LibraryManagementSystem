package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/** Combined search page: books, members and transactions in one place. */
public class SearchServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type"); // books, members, transactions
        String keyword = request.getParameter("keyword");

        List<Book> books = Collections.emptyList();
        List<Member> members = Collections.emptyList();
        List<Transaction> transactions = Collections.emptyList();

        if (keyword != null && !keyword.trim().isEmpty() && type != null) {
            switch (type) {
                case "books":
                    books = bookDAO.searchBooks(keyword.trim());
                    break;
                case "members":
                    members = memberDAO.searchMembers(keyword.trim());
                    break;
                case "transactions":
                    transactions = transactionDAO.searchTransactions(keyword.trim());
                    break;
            }
        }

        request.setAttribute("books", books);
        request.setAttribute("members", members);
        request.setAttribute("transactions", transactions);
        request.setAttribute("type", type);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("search.jsp").forward(request, response);
    }
}
