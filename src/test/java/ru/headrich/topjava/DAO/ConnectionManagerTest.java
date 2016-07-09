package ru.headrich.topjava.DAO;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by Montana on 17.06.2016.
 */
public class ConnectionManagerTest{
    @Test
    public void setConnectionProps() throws Exception {

    }

    @Test
    public void getInstance() throws Exception {
        Properties cp = new Properties();
        cp.setProperty("url","jdbc:mysql://localhost:3306/calories");
        cp.setProperty("username","root");
        cp.setProperty("password","root");
        cp.setProperty("driver","com.mysql.jdbc.Driver");

        assertNotNull(ConnectionManagerFactory.getConnectionManager("DV"));
    }

    @Test
    public void getConnection() throws Exception {
        Properties cp = new Properties();
        cp.setProperty("url","jdbc:mysql://localhost:3306/calories");
        cp.setProperty("username","root");
        cp.setProperty("password","root");
        cp.setProperty("driver","com.mysql.jdbc.Driver");

        ConnectionManager cm = ConnectionManagerFactory.getConnectionManager("DV");
        assertNotNull(cm.getConnection());

    }

    @Test
    public void reconnect() throws Exception {

    }

    @Test
    public void disconnect() throws Exception {

    }

}