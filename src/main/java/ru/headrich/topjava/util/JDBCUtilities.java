package ru.headrich.topjava.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.sun.istack.internal.Nullable;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Properties;



/**
 * Created by Montana on 19.06.2016.
 */
public class JDBCUtilities {

 public static DataSource lookUpDataSource()  {
     DataSource ds=null;
     try {
         ds = InitialContext.doLookup("jdbc/topjava");
     } catch (NamingException e) {e.printStackTrace();

     }

     return ds;

}
    public static void bindName(){
//todo почему же mysql, сделать установку извне

        try {
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setUser("root");
            mysqlDataSource.setPassword("root");
            mysqlDataSource.setURL("jdbc:mysql://localhost:3306/calories");
            SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
            builder.bind("jdbc/topjava", mysqlDataSource);
            builder.activate();
            //ctx.bind("jdbc/topjava",obj);
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
    }


}
