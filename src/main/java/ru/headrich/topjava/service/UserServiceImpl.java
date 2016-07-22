package ru.headrich.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.util.converters.PasswordEncryption;
import ru.headrich.topjava.util.exception.NotFoundException;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */

@Service
public class UserServiceImpl implements UserService {

    //@Resource(name = "userRepository") //это di, а так же сеттер, конструктор, или получение контроллера фабричным методом.

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl() {
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return userRepository.getUser(id);
    }

    @Override
    public void remove(int id) throws NotFoundException {
        userRepository.deleteUser(id);
    }

    @Override
    public User getByEmail(String email) throws NotFoundException {
        return userRepository.getByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public boolean update(User user) throws NotFoundException {
        // if is new -save ? else - update  on service method
        return userRepository.updateUser(user);
    }

    @Override
    public User getByName(String username) {
        return userRepository.getByName(username);
    }

    @Override
    public boolean authentificateUser(User u, String password) throws Exception {
        return PasswordEncryption.getEncryptor().authenticate(password,u.getPassword());
    }

    @Override
    public boolean isUpdated() {

        return false;
    }
}
