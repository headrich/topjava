package ru.headrich.topjava.DAO.ORMengine.aspects;

import org.junit.Before;
import org.junit.Test;
import ru.headrich.topjava.model.BaseEntity;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;

import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by Montana on 23.06.2016.
 */
public class TestAspectTest {
    User u1;
    UserMeal um1;
    @Before
    public void setUp() throws Exception {
        u1 = new User("Anton","gedn@gedn.com","fdfdf", Role.ROLE_ADMIN);
        um1  = new UserMeal(new Date(System.currentTimeMillis()),"almagel",5);
    }


    @Test
    public void catchChanghesAdvice() throws Exception {
        User u1 = new User();
        u1.setEmail("Krut");
       // um1.setCaloiries(100);
        System.out.println(u1.getEmail());

        assertTrue( u1.getEmail().equals("Krut"));
    }

}