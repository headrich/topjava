package ru.headrich.topjava.DAO.RDB.factory;

import ru.headrich.topjava.DAO.DAOFactory;
import ru.headrich.topjava.DAO.UserDAO;
import ru.headrich.topjava.DAO.UserMealDAO;
import ru.headrich.topjava.model.UserMeal;

/**
 * Created by Montana on 11.06.2016.
 */
public class DerbyDAOFactory extends DAOFactory {


    @Override
    public UserDAO getUserDAO() {
        return null;
    }

    @Override
    public UserMealDAO getUserMealDAO() {
        return null;
    }
}
