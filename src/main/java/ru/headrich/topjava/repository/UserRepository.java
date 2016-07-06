package ru.headrich.topjava.repository;

import ru.headrich.topjava.model.User;

import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */
//кароче то дао которое я использваол в ранних проектах, было размытым , не настоящим дао, а смесью дао и репозитория.
// Т.к. тру дао это только CRUD и реализации для разных СУБД.

public interface UserRepository {

    User save(User user);

    User get(int id);

    boolean delete(int id);

    User getByEmail(String email);
    User getByName(String name);

    List<User> getAllUsers();
}
