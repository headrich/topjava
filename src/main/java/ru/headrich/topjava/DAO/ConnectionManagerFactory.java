package ru.headrich.topjava.DAO;

import com.sun.istack.internal.Nullable;

import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by Montana on 09.07.2016.
 */
public class ConnectionManagerFactory {

private static DataSourceConnectionManager dscm;
private static DriverManagerConnectionManager dvcm;
    private static Properties cp; //откуда берутся???
    static {
        cp = new Properties();
        cp.setProperty("url", "jdbc:mysql://localhost:3306/calories?allowMultiQueries=true");
        cp.setProperty("username", "root");
        cp.setProperty("password", "root");
        cp.setProperty("driver", "com.mysql.jdbc.Driver");
    }

    public static  ConnectionManager getConnectionManager(@Nullable String type){
        switch(type){
            case "DS": return (dscm==null) ? new DataSourceConnectionManager() : dscm;
            case "DM": return (dvcm == null)? new DriverManagerConnectionManager() : dvcm;
            default:return (dvcm == null)?  DriverManagerConnectionManager.getInstance(cp) : dvcm;
        }
    }

}
