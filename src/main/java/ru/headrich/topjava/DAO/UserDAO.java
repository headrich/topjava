package ru.headrich.topjava.DAO;

import ru.headrich.topjava.model.User;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Montana on 07.06.2016.
 */
public interface UserDAO {

    //CRUD

    User getUser(int id) throws SQLException;
    boolean deleteUser(int id);
    User saveUser(User user);
    void updateUser(User user) throws SQLException;
    Collection<User> listUsers();



}
