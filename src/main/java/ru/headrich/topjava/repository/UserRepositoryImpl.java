package ru.headrich.topjava.repository;

import ru.headrich.topjava.model.User;

import java.util.List;

/**
 * Created by Montana on 30.06.2016.
 */
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User save(User user) {

        return null;
    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}
