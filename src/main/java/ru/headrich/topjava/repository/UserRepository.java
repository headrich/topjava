package ru.headrich.topjava.repository;

import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */
//кароче то дао которое я использваол в ранних проектах, было размытым , не настоящим дао, а смесью дао и репозитория.
// Т.к. тру дао это только CRUD и реализации для разных СУБД.

public interface UserRepository {

    User addUser(User user);

    User getUserById(int id);

    void updateUser(User user);

    boolean deleteUser(int id);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getUserByName(String name);

    Collection<User> listUsersByRole(Role role);




}
