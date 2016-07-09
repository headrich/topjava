package ru.headrich.topjava.DAO;

import com.sun.istack.internal.Nullable;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Montana on 09.07.2016.
 */
interface ConnectionManager {
    public Connection getConnection() ;

    Map<String,List<List<String>>> getSchema();

    void disconnect();
}
