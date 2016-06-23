package ru.headrich.topjava.DAO.ORMengine.Session;

import java.sql.Connection;

/**
 * Created by Montana on 21.06.2016.
 */
public class SessionImpl implements Session {

    private  Connection connection;
    //bean - bean-scope - prototype

    public SessionImpl() {
        connection = null;
    }

    @Override
    public Connection getCurrentConnection() {
        return null;
    }

    @Override
    public void startTransaction() {

    }

    @Override
    public Transaction getTransaction() {
        return null;
    }


}
