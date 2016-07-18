package ru.headrich.topjava.repository.JPA;

import org.hibernate.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.UserRepository;
import ru.headrich.topjava.util.JPAHandlerUtil;
import ru.headrich.topjava.util.exception.NotFoundException;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Montana on 30.06.2016.
 */
public class UserRepositoryImpl implements UserRepository {

    private static Logger LOG = LoggerFactory.getLogger(UserRepositoryImpl.class);
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

    //TODO ТУТ ВЕЗДЕ ДЕТАЧЕД ОБХЕКТЫ передаются и используются.
    //нужно помнить об анитпаттерне session per operation и сделать как -то классно.
    @Override
    public User addUser(User user) {
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
    public User getUser(int id) {
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

            System.err.println(e.getMessage() + "\n" + e.getStackTrace());
        }
        finally {

        }
        return u;
    }

    @Override
    //hiberstyle
    public boolean updateUser(User user) {
        boolean updated=false;
        //Session s = emf.createEntityManager().unwrap(Session.class);
        //Illegal attempt to associate a collection with two open sessions. Collection : [ru.headrich.topjava.model.User.authorities#46]
        //todo у меня ождна сессия - которая загрузила авторити для пользователя, другая пытается их обновить. такие дела. как решать?
        Session s=null;
        try{
            s=JPAHandlerUtil.buildSessionFactory().getCurrentSession();
            LOG.info("updateUser: Get Current session "  + s );
        }catch (HibernateException e){
            LOG.info("updateUser: Open new session "  + s );
            s= JPAHandlerUtil.buildSessionFactory().openSession();
        }
        //s.merge(user);

        //todo is this session equals session retrived by unwraping em??
        //System.out.println("sessions emf vs sf   s equals s1  : " +s.equals(JPAHandlerUtil.buildSessionFactory().getCurrentSession()));
        Transaction transaction = s.getTransaction();
        try{
            transaction.begin();

            s.update(user);
            transaction.commit();
            updated=true;
        }catch (Exception e ){
            transaction.rollback();
            updated =false;
            System.err.println(e.getMessage() + "\n"  );
            Stream.of(e.getStackTrace()).forEach(System.out::println);
        }finally {
            //todo if session close then try-wuthresourceblock else disconnect ??
           // Connection connection = s.disconnect();
            //java.lang.IllegalStateException: Session/EntityManager is closed

        }

        return updated;
    }


    @Override
    public boolean deleteUser(int id) {
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
        Transaction transaction=null;
        try {
            Session s = em.unwrap(Session.class);

            //Session s=null;

            //может быть так что запрос мы можем постоянно обрабатывать forward и include/ и сессия будет оставаться в раммках этого запроса.
            //но лучше сделать сессию в рамках хттп сессии.
            try{
                s=JPAHandlerUtil.buildSessionFactory().getCurrentSession();
                LOG.info("getUserByEmail: Get Current session "  + s );
            }catch (HibernateException e){
                LOG.info("getUserByEmail: Open new session "  + s );
                s= JPAHandlerUtil.buildSessionFactory().openSession();
            }
            try{
                transaction = s.getTransaction();
                LOG.info("getUserByEmail: Get current transaction "  + transaction );
                transaction.begin();
            }catch (Exception e){
                transaction= s.beginTransaction();
                LOG.info("getUserByEmail: Begin new transaction "  + transaction );
            }
            users = s.createNamedQuery(User.ByEmail,User.class).setParameter("email",email).getResultList();
            transaction.commit();

        }catch (Exception e ){
            //log
            transaction.rollback();
            System.out.println(e.getMessage());

        }

        User u = users.stream().findFirst().orElse(null);
      //  if(u==null) throw new NotFoundException("User with email "+ email + " not found");
        return u;
    }

    @Override
    public User getByName(String name) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();

        List<User> users = null;
        Transaction t = null;
        try {
            Session s = em.unwrap(Session.class);
            try{
                s=JPAHandlerUtil.buildSessionFactory().getCurrentSession();
                LOG.info("getUserByName: Get Current session "  + s );
            }catch (HibernateException e){
                LOG.info("getUserByName: Open new session "  + s );
                s= JPAHandlerUtil.buildSessionFactory().openSession();
            }

            try{
                t = s.getTransaction();
                LOG.info("getUserByName: Get current transaction "  + t );
                t.begin();
            }catch (Exception e){
                t= s.beginTransaction();
                LOG.info("getUserByName: Begin new transaction "  + t );
            }

            //users = em.createNamedQuery(User.ByName,User.class).setParameter("name",name).getResultList(); //нет LazyInitiException for authorities(fetchtype = lazy)
            s.enableFetchProfile("userRolesFP"); //альтернатива графам в хибернейте

            users = s.createNamedQuery(User.ByName,User.class).setParameter("name",name).getResultList(); //выбрасывается lazyinitexcept
            if(s.isFetchProfileEnabled("userRolesFP")) s.disableFetchProfile("userRolesFP");

            t.commit();
        }catch (Exception e ){
            t.rollback();
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
