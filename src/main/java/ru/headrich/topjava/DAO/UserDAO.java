package ru.headrich.topjava.DAO;

import ru.headrich.topjava.model.User;

import java.util.Collection;

/**
 * Created by Montana on 07.06.2016.
 */
public interface UserDAO {

    //CRUD

    User getUser(int id);
    boolean deleteUser(int id);
    User saveUser(User user);
    void updaateUser(User user);
    Collection<User> getAllUsers();


}
