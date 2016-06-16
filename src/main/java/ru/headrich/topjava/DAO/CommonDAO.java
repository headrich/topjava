package ru.headrich.topjava.DAO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Montana on 15.06.2016.
 */
public interface CommonDAO<T,PK extends Serializable> {

    public T getById(int id) throws SQLException;
    public  T save(T entity);
    public List<T> getAll(Class<T> clazz);
    public  void  update(T entity);
    public  boolean delete(int id);

    //generic crud operations
}
