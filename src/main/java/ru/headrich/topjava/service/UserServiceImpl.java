package ru.headrich.topjava.service;

import ru.headrich.topjava.model.User;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.util.exception.NotFoundException;

import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User get(int id) throws NotFoundException {
        return null;
    }

    @Override
    public void remove(int id) throws NotFoundException {

    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void update(User user) throws NotFoundException {

    }
}
