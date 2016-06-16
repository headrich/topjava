package ru.headrich.topjava.DAO.RDB.factory;

import ru.headrich.topjava.DAO.DAOFactory;
import ru.headrich.topjava.DAO.RDB.userdao.MysqlUserDAO;
import ru.headrich.topjava.DAO.RDB.usermealdao.MysqlUserMealDAO;
import ru.headrich.topjava.DAO.UserDAO;
import ru.headrich.topjava.DAO.UserMealDAO;
import ru.headrich.topjava.model.UserMeal;

/**
 * Created by Montana on 11.06.2016.
 */
public class MysqlDAOFactory extends DAOFactory {

protected final static String driver = "com.mysql.jdbc.Driver";

    @Override
    public UserDAO getUserDAO() {
        return new MysqlUserDAO();
    }

    @Override
    public UserMealDAO getUserMealDAO() {
        return new MysqlUserMealDAO();
    }

}
