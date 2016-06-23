package ru.headrich.topjava.repository;

import ru.headrich.topjava.DAO.UserDAO;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Created by Montana on 22.06.2016.
 */
public class UserRepositoryImpl implements UserRepository{

    UserDAO userDAO;
//вот он наш DI
    public UserRepositoryImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User addUser(User user) {
        return userDAO.saveUser(user);
    }

    @Override
    public User getUserById(int id)  {
        try {
            return userDAO.getUser(id);
        } catch (SQLException e) {
            //тут типа как у кислина ошибки уже более высокого уровня. такие чтоб яснопонятно для клиента.
            //хотя у него там на уровне сервися это все. что верно.

           System.out.print("ОШИБКА в уровне репозитория. Не найден пользователь с айди. " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
        try {
            userDAO.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userDAO.listUsers();
    }

    //ну тут тоже обобщить надо. причем в слое дао конечно. тут все прозрачно должно быть.
    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public User getUserByName(String name) {
        return null;
    }

    @Override
    public Collection<User> listUsersByRole(Role role) {
        return null;
    }
}
