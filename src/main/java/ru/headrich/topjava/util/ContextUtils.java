package ru.headrich.topjava.util;

import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Hashtable;

/**
 * Created by Montana on 09.07.2016.
 */
public class ContextUtils {
    public static final int MysqlTopjavaDataSource = 1;

    public static DataSource lookUpDataSourceName(int type) throws NamingException {
        //http://stackoverflow.com/questions/5861874/easy-way-to-start-a-standalone-jndi-server-and-register-some-resources
        DataSource ds = null;
        switch(type) {
            case 1:   //1

                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
                env.put(Context.PROVIDER_URL, "rmi://127.0.0.1:1099");
                InitialContext ctx = new InitialContext(env);
                Context webContext = (Context)ctx.lookup("java:/comp/env");
                 ds = (DataSource) webContext.lookup("jdbc/topjava");
           default:

        }

        return ds;
    }
}
