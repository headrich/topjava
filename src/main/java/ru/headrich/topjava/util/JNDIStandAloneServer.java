package ru.headrich.topjava.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.Callable;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.jnp.server.NamingServer_Stub;
import org.jnp.server.*;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * Created by Montana on 09.07.2016.
 */
public class JNDIStandAloneServer implements Callable<Object> {
    public final static int RMIPROVIDER =1;
    public final static int APACHE =2;
    public final static int SPRING =3;
    public final static int CUSTOM =4;
    public void initServer(int type) throws NamingException {
        Hashtable env = new Hashtable();
        switch(type) {
            case 1:
                    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
                     env.put(Context.PROVIDER_URL, "rmi://server:1099");
            case 2:

                env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
                env.put(Context.URL_PKG_PREFIXES, "org.apache.naming");
            case 3:
                SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
                //builder.bind("jdbc/Oracle", ods);
                //builder.activate();

        }
        System.setProperties((Properties) env);
        InitialContext ic = new InitialContext();
        ic.createSubcontext("java:");
        ic.createSubcontext("java:/comp");
        ic.createSubcontext("java:/comp/env");
        ic.createSubcontext("java:/comp/env/jdbc");

//todo todom
        //ic.createSubcontext("java:/comp/env/jdbc/mysql");
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setURL("jdbc:mysql://localhost:3306/calories");
        ic.bind("java:/comp/env/jdbc/topjava",mysqlDataSource);
        //rebind





    }


    @Override
    public Object call() throws Exception {
        initServer(1);
        return null;
    }
}
