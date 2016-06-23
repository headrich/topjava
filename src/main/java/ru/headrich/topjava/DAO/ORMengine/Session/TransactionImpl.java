package ru.headrich.topjava.DAO.ORMengine.Session;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Created by Montana on 21.06.2016.
 */
public class TransactionImpl implements Transaction {

    protected final Connection connection;
    private TransactionImpl(Connection con){
        this.connection = con;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    @Override
    public void setSavePoint() throws SQLException {
        connection.setSavepoint();
    }

    @Override
    public void realeseSavePoint(Savepoint savepoint) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }
}
