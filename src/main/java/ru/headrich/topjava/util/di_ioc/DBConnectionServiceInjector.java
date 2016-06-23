package ru.headrich.topjava.util.di_ioc;

import ru.headrich.topjava.DAO.DAOFactory;
import ru.headrich.topjava.DAO.SimpleDAOFactory;

/**
 * Created by Montana on 22.06.2016.
 */
public class DBConnectionServiceInjector<T extends  DAOFactory> implements ConnectionServiceInjector<T> {
    T DAOFactory;

    @Override
    public T getConsumer() {
        return  null;
    }


}
