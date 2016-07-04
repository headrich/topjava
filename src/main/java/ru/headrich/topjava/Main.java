package ru.headrich.topjava;

import ru.headrich.topjava.DAO.UserDAO;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.JPA.UserMealRepositoryImpl;
import ru.headrich.topjava.repository.UserMealRepository;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.repository.JPA.UserRepositoryImpl;
import ru.headrich.topjava.util.JPAHandlerUtil;
import ru.headrich.topjava.util.converters.PasswordEncryption;

import java.util.Arrays;
import java.util.HashSet;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.format("Hello Topjava Enterprise!");
        String hashp = PasswordEncryption.getEncryptor().getHash("201941monstr");
        System.out.println(PasswordEncryption.getEncryptor().authenticate("201941monstr",hashp));
        System.out.println( Role.ROLE_ADMIN.ordinal());

        UserRepository userDAO = new UserRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory());
        UserMealRepository userMealRepository = new UserMealRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory());


        UserMeal um = new UserMeal();
        um.setDescription("wi32132344АА444th321u35");

        User nu = new User();
        //um.setUser(nu);
        //um.setUser(userDAO.get(40));
        nu.setEmail("eeee");

        userMealRepository.addMeal(um,1);

        //userDAO.save(nu);

        //userDAO.get(1);
        //System.out.println(userDAO.getAllUsers().get(0).getMeals());


    }
}
