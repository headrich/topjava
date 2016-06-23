package ru.headrich.topjava.DAO.ORMengine.Session;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Created by Montana on 21.06.2016.
 */
public interface Transaction {

    void commit() throws SQLException;
    void rollback() throws SQLException;
    void setSavePoint() throws SQLException;
    void realeseSavePoint(Savepoint savepoint)throws SQLException;
}
