package ru.headrich.topjava.DAO.ORMengine.Session;

import java.sql.Connection;

/**
 * Created by Montana on 21.06.2016.
 */

//будет хранить в себе информацию о состоянии объектов сущностей,
// получать и выдавать соедниние из пула ? -вполне.
    //а так же можно сюда добавить некоторые общие черты, характерные для всех запросов. получение соединения  , кстати, одна из них.

    //по сути в хибернейте - это и есть обобщенный дао. DAOFactory.getcurrentDAO() звучит не очень, а вот getCurrentSession норм. Значит все же DAOFactory это нормас.
    // а значит и инфу о соединении хранить в реализации дао тоже вполне можно.
    //у них еще интерфейс для работы с транзакциями есть. Он походу сделан в виде паттерна какого-то,который прячет в себе вызовы connection.setautocommit. rollback commit...
    //фасад типа
public interface Session {

    public Connection getCurrentConnection();
    public void startTransaction();
    Transaction getTransaction();

    //crud




}
