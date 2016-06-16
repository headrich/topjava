package ru.headrich.topjava.DAO.RDB;

import ru.headrich.topjava.DAO.CommonDAO;
import ru.headrich.topjava.model.User;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Montana on 15.06.2016.
 */
public interface CommonRDBDAO<T,PK extends Serializable> extends CommonDAO<T,PK> {

    Connection connection = null;

    @Override
    default T getById(int id) throws SQLException {
        return null;
    }

    @Override
    default T save(T entity) {
        return null;
    }

    @Override
    default List getAll(Class<T> clazz) {
        return null;
    }

    @Override
    default void update(T entity) {

    }

    @Override
    default boolean delete(int id) {
        return false;
    }
}
