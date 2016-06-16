package ru.headrich.topjava.DAO.RDB.userdao;

import ru.headrich.topjava.DAO.RDB.CommonRDBDAO;
import ru.headrich.topjava.DAO.RDB.RDBBaseDAO;
import ru.headrich.topjava.DAO.UserDAO;
import ru.headrich.topjava.DAO.UserDAOImpl;
import ru.headrich.topjava.model.User;

import java.sql.Connection;
import java.util.Collection;

/**
 * Created by Montana on 11.06.2016.
 */
public class MysqlUserDAO extends RDBBaseDAO implements CommonRDBDAO<User,Integer> {


    @Override
    public Connection getConnection() {

        return null;
    }



}
