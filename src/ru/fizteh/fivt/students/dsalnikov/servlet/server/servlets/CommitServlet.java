package ru.fizteh.fivt.students.dsalnikov.servlet.server.servlets;

import ru.fizteh.fivt.students.dsalnikov.servlet.database.Transaction;
import ru.fizteh.fivt.students.dsalnikov.servlet.database.TransactionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommitServlet extends AbstractHttpServletWrapper {

    public CommitServlet(TransactionManager manager) {
        super(manager);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Transaction transaction = getTransaction(req, resp);
        int result = transaction.commit();

        setUpResponse(resp);

        resp.getWriter().println(String.format("%s=%d", Paths.DIFF, result));
    }
}
