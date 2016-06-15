package ru.headrich.topjava.DAO;

import ru.headrich.topjava.DAO.RDB.factory.DerbyDAOFactory;
import ru.headrich.topjava.DAO.RDB.factory.MysqlDAOFactory;
import ru.headrich.topjava.DAO.RDB.factory.OracleDAOFactory;
import ru.headrich.topjava.DAO.RDB.factory.PGDAOFactory;
import ru.headrich.topjava.model.User;

/**
 * Created by Montana on 11.06.2016.
 */
public abstract class DAOFactory {

    // Список типов DAO, поддерживаемых генератором
    public static final int MYSQL = 1;
    public static final int ORACLE = 2;
    public static final int DERBY = 3;
    public static final int PG = 4;


    public abstract UserDAO getUserDAO();
    public abstract UserMealDAO getUserMealDAO();


    public static DAOFactory getFactory(int type){
        switch(type){
            case MYSQL: return new MysqlDAOFactory();

            case ORACLE: return new OracleDAOFactory();

            case DERBY: return new DerbyDAOFactory();

            case PG: return new PGDAOFactory();
            default:return null;
        }
    }



}
