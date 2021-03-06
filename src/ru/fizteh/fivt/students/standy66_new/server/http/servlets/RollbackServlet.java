package ru.fizteh.fivt.students.standy66_new.server.http.servlets;

import ru.fizteh.fivt.students.standy66_new.server.tdb.Transaction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author andrew
 *         Created by andrew on 30.11.14.
 */
public class RollbackServlet extends BaseDbServlet {
    public RollbackServlet(DbBinder dbBinder) {
        super(dbBinder);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Transaction transaction = getTransaction(req, res);
        if (transaction != null) {
            int noOfChanges = transaction.rollback();
            res.setStatus(200);
            res.getWriter().write("OK: changes dismissed: " + noOfChanges);
        } else {
            sendErrorNoTransaction(res);
        }
    }
}
