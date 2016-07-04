package ru.headrich.topjava.repository.JPA;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;

import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.util.JPAHandlerUtil;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Connection;
import java.util.*;

/**
 * Created by Montana on 30.06.2016.
 */
public class UserRepositoryImpl implements UserRepository {
    EntityManagerFactory emf;
    Connection currentConnection;
    Session s;

    public UserRepositoryImpl() {
        //так мы породим лишние зависимости
        //emf = JPAHandlerUtil.buildEntityManagerFactory();
    }

    public UserRepositoryImpl(EntityManagerFactory emf) {
        //так мы внедряем зависимость
        this.emf = emf;
    }

    //нужно помнить об анитпаттерне session per operation и сделать как -то классно.
    @Override
    public User save(User user) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        if(s==null || !s.isOpen()) {
            s=em.unwrap(Session.class);
        }else
            if(currentConnection!=null && !s.isConnected()) s.reconnect(currentConnection);

        //for JTA transactions only
        //if(!em.isJoinedToTransaction()) em.joinTransaction();
        EntityTransaction etr = em.getTransaction();
        try {
            etr.begin();
            em.persist(user);
            etr.commit();
        }catch (Exception e ){
            System.err.println(e.getMessage());
            etr.rollback();
        }finally {
            currentConnection = s.disconnect();
        }
        return user;
    }

    //EntityGraphs - для того что изменять маппинг в рантайме. Что позволит улучшить производительность в некоторых случаях.
    // Например мы можем получить лишь несколько указанных полей (EAGER), в то время как замапленно все..Или тот что был Lazy станет Eager.
    //манипуляции позволяют контролировать число запросов к базе. И получать нужную выборку за n число запросов
    //hints : fetchgraph - игнор дефаулт. loadgraph - добавляем к дефолтному.
    @Override
    public User get(int id) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        User u = null;
        try {

            /*EntityGraph<User> eg = em.createEntityGraph(User.class);
            eg.addAttributeNodes("authority");
            Map<String,Object> ps = new HashMap<>();
            ps.put("javax.persistence.loadgraph",eg);
            //загрузит роли EAGER(сразу), хотя в дефолтном маппинге они LAzy/
            // В hibernate API для этого есть @FetchProfile
            u =  em.find(User.class, id, ps);*/
            //без запросов к бд,получаем прокси:
           /* u =  em.getReference(User.class, id);
            Session s = em.unwrap(Session.class);
            //ту ошибка будет что объект юзер с ид 45 не найден, если попытаемя обратиться к свойствам
            s.load(User.class,45);*/
            //QUERY HINTS - надстройки для запросов. "javax.persistence.loadgraph" - это hint  . они разные для провайдеров
            //"javax.persistence.query.timeout", javax.persistence.lock.timeout - тоже.
            // Можно создавать emfactory  Persistence.createEntityManagerFactory(unit, hints);
            // и EManager emf.createEntityManager(hints); или к запросу  em.CreateQuery.setHint("hint",value);
            Map<String,Object> queryHints = new HashMap<>();
            queryHints.put("javax.persistence.loadgraph",em.createEntityGraph("userRoleGraph"));

            Session s = em.unwrap(Session.class);
            s.enableFetchProfile("userMealsFP"); //альтернатива графам в хибернейте

            u=em.find(User.class,id,queryHints);
            if(s.isFetchProfileEnabled("userMealsFP")) s.disableFetchProfile("userMealsFP");
            s.close();

            //s.createFilter(u.getMeals(),"where this.caloiries between :min and :max").setParameter("min",50).setParameter("max",100).list();


        }catch (Exception e ){
            //log
            System.err.println(e.getMessage());
        }
        finally {

        }
        return u;
    }


    @Override
    public boolean delete(int id) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        boolean deleted=false;
        //for JTA transactions only
        //if(!em.isJoinedToTransaction()) em.joinTransaction();
        EntityTransaction etr = em.getTransaction();
        Session s = em.unwrap(Session.class);


        //User u = new User();
        //u.setId(89);
        //u = em.merge(u); //робит, но не будет ошибки норм есл нет такого
        //u = em.getReference(User.class,89); // с ошибкой что такойго нет при удалении
        //em.refresh(u);
        try {
            etr.begin();
            //em.remove(u);
            deleted = em.createNamedQuery(User.DELETE).setParameter("id",id).executeUpdate()!=0;
            etr.commit();
        }catch (Exception e ){
            System.out.println(e.getMessage());
            etr.rollback();
        }
        return deleted;
    }

    @Override
    public User getByEmail(String email) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();

        List<User> users = null;

        try {
            Session s = em.unwrap(Session.class);


            users = em.createNamedQuery(User.ByEmail,User.class).setParameter("email",email).getResultList();
        }catch (Exception e ){
            //log
            System.out.println(e.getMessage());
        }
        return users.stream().findFirst().orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();

        List<User> users = null;

        try {
            Session s = em.unwrap(Session.class);//TODO взглянуть как конфигурировать hiber на сессии а не на ем
            //попробуем с фильтром
            //фильтры для дочерних коллекций выставляются над ними, а не над доменной сущностью иначе поля будет искать в user а не в meal
            //s.enableFilter("mealsDateFilter").setParameter("mealsDateFilterParam",new Date(1992-02-04));
            //s.enableFilter("mealsCaloriesRange").setParameter("min",50).setParameter("max",100);


            users = em.createNamedQuery(User.ALL).getResultList();


        }catch (Exception e ){
            //log
            System.out.println(e.getMessage());
        }
        return users;
    }
}
