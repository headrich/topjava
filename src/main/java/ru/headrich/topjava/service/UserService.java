package ru.headrich.topjava.service;

import ru.headrich.topjava.model.User;
import ru.headrich.topjava.util.exception.NotFoundException;

import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */
public interface UserService {
    User save(User user);

    User get(int id) throws NotFoundException;

    void remove(int id) throws NotFoundException;

    User getByEmail(String email) throws NotFoundException;

    List<User> getAllUsers();

    void update(User user)throws NotFoundException;

    User getByName(String username);

    boolean authentificateUser(User u,String password) throws Exception;

    boolean isUpdated();
}
