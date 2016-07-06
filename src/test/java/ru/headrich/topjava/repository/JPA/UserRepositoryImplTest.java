package ru.headrich.topjava.repository.JPA;

import org.junit.Before;
import org.junit.Test;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.UserMealRepository;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.util.JPAHandlerUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Montana on 03.07.2016.
 */
public class UserRepositoryImplTest {
    User u = getTestUser();
    UserRepository userR;
    UserMealRepository userMealR;

    //// TODO: 04.07.2016  Create testparameters to process any entity states(transient,removed,persistent,detached) its great  
    @Before
    public void setUp() throws Exception {
        userR = new UserRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory());
        userMealR = new UserMealRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory());



    }
    public static User getTestUser() {
        UserMeal um = new UserMeal(new Date(),"default description",100);
        User u = new User("Testfd"+new Random().nextInt(),"Karaganda@gmail.ru"+ new Random().nextInt(),"12345", Role.ROLE_USER);
        um.setUser(u);
        u.setMeals(Arrays.asList(um));
        return u;
    }


    @Test
    public void save() throws Exception {
        assertTrue(userR.save(u).getMeals().get(0).getId()!=null);
    }

    @Test
    public void get() throws Exception {
        assertTrue(userR.get(1)!=null);
    }

    @Test
    public void delete() throws Exception {
        assertTrue(userR.delete(39));
    }

    @Test
    public void getByEmail() throws Exception {
        assertEquals(userR.getByEmail("Karaganda@gmail.ru-637590599").getId().intValue(),48);
    }

    @Test
    public void getAllUsers() throws Exception {
        assertTrue(userR.getAllUsers().size()>10);
    }

}