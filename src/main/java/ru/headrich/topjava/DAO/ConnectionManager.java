package ru.headrich.topjava.DAO;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by Montana on 09.06.2016.
 */
public class ConnectionManager {
    public static final String[] propKeys = {"driver","url","username","password"};
    private  static ConnectionManager cmInstance;
    private  static Connection connection;
    private static  String jdbcDriver = "com.mysql.jdbc.Driver";
    private String url;
    private String username;
    private String password;

    private Properties conProps;

    //класс соединения с базой. Для операций/запросов будем использовать ДАО. чтоб красиво было все.
    private ConnectionManager(Properties properties) {

        this.conProps=properties;
        if(conProps.containsKey("driver")) this.jdbcDriver = conProps.getProperty("driver");
        if(conProps.containsKey("url")) this.url = conProps.getProperty("url");
        if(conProps.containsKey("username")) this.username = conProps.getProperty("username");
        if(conProps.containsKey("password")) this.password = conProps.getProperty("password");

    }
    //меняем параметры соединения. нужен реконнект.
    public void setConnectionProps(Properties properties){
        this.conProps=properties;
        if(conProps.containsKey("driver")) this.jdbcDriver=conProps.getProperty("driver");
        if(conProps.containsKey("url")) this.url = conProps.getProperty("url");
        if(conProps.containsKey("username")) this.username = conProps.getProperty("username");
        if(conProps.containsKey("password")) this.password = conProps.getProperty("password");

    }
        //тут все и сразу потому делаем синглтоном.
    public static ConnectionManager getInstance(Properties properties){

        if(cmInstance==null){
            synchronized (ConnectionManager.class){
                if(cmInstance==null){

                    cmInstance=new ConnectionManager(properties);
                }
            }
        }
        return cmInstance;
    }


    public Connection getConnection() throws ClassNotFoundException, SQLException {

        //проверка на наличие зарегестрированного драйвера
        if(connection==null || connection.isClosed()) {
            Enumeration<Driver> drvs = DriverManager.getDrivers();
            while (drvs.hasMoreElements()) {
                if (drvs.nextElement().getClass().equals(jdbcDriver)) {
                    break;
                } else {
                    Class.forName(jdbcDriver);
                    break;
                }
            }

            return DriverManager.getConnection(url, username, password);
        }return connection;

    }

    public Connection reconnect() throws SQLException, ClassNotFoundException {
        if(connection!=null){
                if(!connection.isClosed()){
                    connection.close();
                }
        }

        return getConnection();
    }

    public void disconnect() throws SQLException {
        if(connection!=null){
            if(!connection.isClosed()){
                connection.close();
            }
        }

    }


}
