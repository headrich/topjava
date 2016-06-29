package ru.headrich.topjava.DAO.ORMengine.aspects;

import org.junit.Before;
import org.junit.Test;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;

import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by Montana on 23.06.2016.
 */
public class ModelChangeCaptureAspectTest {
    User u1;
    UserMeal um1;
    @Before
    public void setUp() throws Exception {
        u1 = new User("Anton","gedn@gedn.com","fdfdf", Role.ROLE_ADMIN);
        um1  = new UserMeal(new Date(System.currentTimeMillis()),"almagel",5);
    }

    @Test
    public void catchChanghesAdvice() throws Exception {
        u1.setName("Krut");
        um1.setCaloiries(100);

        assertTrue( u1.getName().contains("oy"));
        assertTrue(new ModelChangeCaptureAspect().getUpdatedFieldsMap().size()!=0);
    }

}