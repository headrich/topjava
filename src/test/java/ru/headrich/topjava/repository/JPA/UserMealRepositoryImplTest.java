package ru.headrich.topjava.repository.JPA;

import org.junit.Before;
import org.junit.Test;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.UserMealRepository;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.util.JPAHandlerUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Montana on 03.07.2016.
 */
public class UserMealRepositoryImplTest {
    UserMeal m = getTestMeal();
    UserRepository userR;
    UserMealRepository userMealR;
    @Before
    public void setUp() throws Exception {
         userR = new UserRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory());
         userMealR = new UserMealRepositoryImpl(JPAHandlerUtil.buildEntityManagerFactory());



    }
    public static UserMeal getTestMeal() {
        UserMeal um = new UserMeal(new Date(),"default description",100);
        User u = new User("Test","Karaganda@gmail.ru"+ new Random().nextInt(),"12345", Role.ROLE_USER);
        um.setUser(u);
        u.setMeals(Arrays.asList(um));
        return um;
    }

    @Test
    public void addMeal() throws Exception {
                //m.setUser(userR.get(46));
            assertEquals(userMealR.addMeal(m,1).getId().intValue(),51);
    }

    @Test
    public void getAllMeals() throws Exception {
        System.out.println(userMealR.getAllMeals());
        assertNotNull(userMealR.getAllMeals());
    }

    @Test
    public void getAllUserMeals() throws Exception {
        System.out.println(userMealR.getAllUserMeals(1));
        assertNotNull(userMealR.getAllUserMeals(1));
    }

    @Test
    public void deleteMeal() throws Exception {

        assertTrue(userMealR.deleteMeal(43));
    }

    @Test
    public void getMeal() throws Exception {
        assertEquals(1,userMealR.getMeal(29).getUser().getId().intValue());
    }

    @Test
    public void getByDate() throws Exception {

    }

    @Test
    public void getByDateRange() throws Exception {
        assertEquals(userMealR.getByDateRange(new Date().from(LocalDateTime.of(2016,06,17,0,0).toInstant(ZoneOffset.UTC)),
                new Date().from(LocalDateTime.of(2016,06,18,0,0).toInstant(ZoneOffset.UTC)),28).size(),2);
    }

    @Test
    public void getByCaloriesRange() throws Exception {
        assertEquals(userMealR.getByCaloriesRange(50,100,1).size(),3);
    }


}