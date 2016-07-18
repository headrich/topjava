package ru.headrich.topjava.repository.JPA;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.UserMealRepository;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.util.JPAHandlerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;
/**
 * Created by Montana on 13.07.2016.
 */

public class EntityStatesTest  {
    UserRepository userR;
    UserMealRepository userMealR;
    SessionFactory sf;
    Logger LOG = LoggerFactory.getLogger(EntityStatesTest.class);
    @Before
    public void setUp() throws Exception {
        sf= JPAHandlerUtil.buildSessionFactory();
        //userR = new UserRepositoryImpl(sf);
        //userMealR = new UserMealRepositoryImpl(sf);

    }
    public static User getTestUser() {
        //UserMeal um = new UserMeal(new Date(),"default description",100);
        User u = new User("Testfd"+new Random().nextInt(),"Karaganda@gmail.ru"+ new Random().nextInt(),"12345", Role.ROLE_USER);
        //um.setUser(u);
        //u.setMeals(Arrays.asList(um));
        ArrayList<UserMeal> meals = new ArrayList<>();
        for(int i=0;i<3;i++){
            meals.add(getTestMeal(u));
        }
        return u;
    }
    public static UserMeal getTestMeal(User u) {
        UserMeal um = new UserMeal(new Date(),"default description",100+new Random(1000).nextInt());
        //User u = new User("Test","Karaganda@gmail.ru"+ new Random().nextInt(),"12345", Role.ROLE_USER);
        um.setUser(u);
        //u.setMeals(Arrays.asList(um));
        return um;
    }


    @Test
    @Transactional
    public void mergeTest(){
        Session session=null;
        //session  = sf.getCurrentSession();
        if (session == null) {
            LOG.info("getCurrentSession return null");
            session=sf.openSession();
        }
        assertNotNull(session);
        //session.setFlushMode(FlushMode.);
        User user = getTestUser();
        Transaction tx = session.beginTransaction();
        session.save(user);
        //session.flush(); //javax.persistence.TransactionRequiredException: no transaction is in progress

        Session session1 = sf.openSession();
        User user1 = session1.get(User.class,user.getId());

        Session session2 = sf.openSession();
        user1.setName("nameS2");
        session2.close();

        tx.commit();


    }
}
