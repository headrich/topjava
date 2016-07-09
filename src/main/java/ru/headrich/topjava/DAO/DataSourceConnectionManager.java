package ru.headrich.topjava.DAO;

import com.sun.istack.internal.Nullable;
import ru.headrich.topjava.util.ContextUtils;
import ru.headrich.topjava.util.JDBCUtilities;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Montana on 09.07.2016.
 */
public class DataSourceConnectionManager implements ConnectionManager {
    //конфиг файл опеределять будет какой ресурс используется. незачем тут устраивать сложные иерархии для получаения того или иного датасурсконнекшнменеджера.
    @Resource
    DataSource ds;

    @Override
    public Connection getConnection()  {
        try {
            return JDBCUtilities.lookUpDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, List<List<String>>> getSchema() {
        return null;
    }

    @Override
    public void disconnect() {

    }

}
