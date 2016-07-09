package ru.headrich.topjava.DAO;

import com.sun.istack.internal.Nullable;

import java.sql.*;
import java.util.*;

/**
 * Created by Montana on 09.06.2016.
 */
public class DriverManagerConnectionManager implements ConnectionManager {
    public static final String[] propKeys = {"driver","url","username","password"};
    private  static ConnectionManager cmInstance;
    private  static Connection connection;
    private static  String jdbcDriver = "com.mysql.jdbc.Driver";
    private String url;
    private String username;
    private String password;


    private Properties conProps;
    private static Properties cp; //откуда берутся???
    static {
        cp = new Properties();
        cp.setProperty("url", "jdbc:mysql://localhost:3306/calories?allowMultiQueries=true");
        cp.setProperty("username", "root");
        cp.setProperty("password", "root");
        cp.setProperty("driver", "com.mysql.jdbc.Driver");
    }
    //класс соединения с базой. Для операций/запросов будем использовать ДАО. чтоб красиво было все.
    public DriverManagerConnectionManager(Properties properties) {

        this.conProps=properties;
        if(conProps.containsKey("driver")) this.jdbcDriver = conProps.getProperty("driver");
        if(conProps.containsKey("url")) this.url = conProps.getProperty("url");
        if(conProps.containsKey("username")) this.username = conProps.getProperty("username");
        if(conProps.containsKey("password")) this.password = conProps.getProperty("password");

    }
    public DriverManagerConnectionManager(){
        //load props from config file
       new DriverManagerConnectionManager(cp);

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
    public static ConnectionManager getInstance(@Nullable Properties properties){

        if(cmInstance==null){
            synchronized (ConnectionManager.class){
                if(cmInstance==null){

                    cmInstance=new DriverManagerConnectionManager(properties);
                }
            }
        }
        return cmInstance;
    }


    public Connection getConnection()  {

        //проверка на наличие зарегестрированного драйвера/ лучше убрать отсюдо
        try {
            if (connection == null || connection.isClosed()) {
                if (loadDriver())
                    return DriverManager.getConnection(url, username, password);
            }
        }catch (Exception e ){

        }

            return connection;

    }

    public boolean loadDriver(){
        Enumeration<Driver> drvs = DriverManager.getDrivers();
        if(drvs.hasMoreElements()) {
            while (drvs.hasMoreElements()) {
                if (drvs.nextElement().getClass().equals(jdbcDriver)) {
                    break;
                } else {
                    try {
                        Class.forName(jdbcDriver);
                    } catch (ClassNotFoundException e) {
                        System.out.print("Driver failed"+ e.getMessage());
                        return false;
                    }
                    break;
                }
            }
        }else{
            try {
                Class.forName(jdbcDriver);
            } catch (ClassNotFoundException e) {
                System.out.print("Driver failed"+ e.getMessage());
                return false;
            }
        }
        return true;
    }

    public Connection reconnect() throws SQLException, ClassNotFoundException {
        if(connection!=null){
                if(!connection.isClosed()){
                    connection.close();
                }
        }

        return getConnection();
    }

    public void disconnect() {
        if(connection!=null){
            try {
                if(!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("CONNECTION ERROR/ CANT disconnect : "+e.getMessage());
            }
        }

    }

    public Map<String,List<List<String>>>  getSchema()  {
        Map<String, List<List<String>>> shemap = null;
        try {
            DatabaseMetaData meta = getConnection().getMetaData();
            shemap = new HashMap<>();
            ResultSet tres = meta.getTables(null, null, "%", null);
            while (tres.next()) {
                String tcat = tres.getString(1);
                String tschema = tres.getString(2);
                String tname = tres.getString(3);
                //System.out.println("Table catalog: [" + tcat + "]; schema: [" + tschema + "]; name: [" + tname + "]");
                //if tablesmap contains tablename
                ResultSet cres = meta.getColumns(tcat, null, tname, null);
                List<List<String>> columns = new LinkedList<>();
                while (cres.next()) {
                    String cname = cres.getString("COLUMN_NAME");
                    String ctype = cres.getString("TYPE_NAME");
                    int csize = cres.getInt("COLUMN_SIZE");
                    //System.out.println("Column name: [" + cname + "]; type: [" + ctype + "]; size: [" + csize + "]");
                    columns.add(Arrays.asList(cname, ctype, String.valueOf(csize)));
                    //if columnlist contains columname
                }
                shemap.put(tname, columns);
            }

        //else throw ex invalid columname  or skip it in the query
        //special for field like authorities

        //создать дерево -каталог, (имя таблицы, (имя поля, тип, размер)) - done
        //потихоньку обобщаем. так круче и интересней
        disconnect();
        System.out.println(shemap);
        }catch (Exception e ){

        }
        return shemap;

    }





}
