package ru.headrich.topjava.util.di_ioc;

import com.sun.xml.internal.ws.server.DefaultResourceInjector;
import ru.headrich.topjava.repository.Repository;

/**
 * Created by Montana on 22.06.2016.
 */
public interface DAOInjector<T extends Repository> extends DefaultInjector<T> {

    T getConsumer();
}
