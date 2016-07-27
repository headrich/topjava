package ru.headrich.topjava.repository.JPA;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.NamedQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.UserMealRepository;
import ru.headrich.topjava.util.JPAHandlerUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import java.sql.Connection;
import java.util.*;

/**
 * Created by Montana on 30.06.2016.
 */
@Repository
public class UserMealRepositoryImpl implements UserMealRepository {

    @Inject
            //обойдемся без Qulifer, т.к исполльзую @Primary
    //@Qualifier(value = "emf") //Spring qualifier позволяет указать имя. в CDI эта аннотация нужна лишь для создания другой аннотации.
    EntityManagerFactory emf;
    Session s;
    Connection currentConnection;


    public UserMealRepositoryImpl() {

        //emf = JPAHandlerUtil.buildEntityManagerFactory();
    }

    public UserMealRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public UserMeal addMeal(UserMeal userMeal,int iduser) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        if(currentConnection!=null){
            if(!s.isConnected())
            s.reconnect(currentConnection);
        }else{
            s = JPAHandlerUtil.getWorkingEntityManager().unwrap(Session.class);
        }
        //for JTA transactions only https://en.wikibooks.org/wiki/Java_Persistence/Transactions#Join_Transaction
        //if(!em.isJoinedToTransaction()) em.joinTransaction();
        EntityTransaction etr = em.getTransaction();
        try{
            etr.begin();
            //em.persist(userMeal);
             s = (Session) em.getDelegate();


           /* if( userMeal.getUser()!=null && !s.contains(userMeal.getUser())){
                userMeal.setUser((User) s.merge(userMeal.getUser()));
            }*///впрочем нам не надо сохранять юзера если его нет. такого впринципе быть не должно. чтобы юзер вне базы сохранял пролдукты.
            //однако если вдруг он у нас детачед?

             s.save(userMeal);//TODO check it with saveorupdate


            etr.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            etr.rollback();
        }finally {
            currentConnection=s.disconnect();
        }
        return userMeal;

    }

    @Override
    public List<UserMeal> getAllMeals() {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        if(currentConnection!=null){
            if(!s.isConnected())
            s.reconnect(currentConnection);
        }else{
            s = JPAHandlerUtil.getWorkingEntityManager().unwrap(Session.class);
        }
        List<UserMeal> meals =null;
        try{

            meals = em.createNamedQuery(UserMeal.All).getResultList();

        }catch (Exception e){
           //log
        }finally {
            currentConnection=s.disconnect();
        }
        return meals;
    }

    @Override
    public List<UserMeal> getAllUserMeals(int iduser) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        if(currentConnection!=null){
            if(!s.isConnected())
            s.reconnect(currentConnection);
        }else{
            s = JPAHandlerUtil.getWorkingEntityManager().unwrap(Session.class);
        }
        List<UserMeal> meals =null;
        try{

            meals = em.createNamedQuery(UserMeal.USERSALLORDERED).setParameter("iduser",iduser).getResultList();
        }catch (Exception e){
            //log
        }finally {
            currentConnection = s.disconnect();
        }
        return meals;
    }

    @Override
    public boolean deleteMeal(int id) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        if(currentConnection!=null){
            if(!s.isConnected())
            s.reconnect(currentConnection);
        }else{
            s = JPAHandlerUtil.getWorkingEntityManager().unwrap(Session.class);
        }
        //for JTA transactions only
        //if(!em.isJoinedToTransaction()) em.joinTransaction();
        EntityTransaction etr = em.getTransaction();
        UserMeal um=null;
        boolean deleted = false;
        try{

            etr.begin();
            //em.remove(um);
            deleted = em.createNamedQuery(UserMeal.DELETE).setParameter("id",id).executeUpdate()!=0;
            //s= (Session) em.getDelegate();
            //s.deleteUser(s.load(UserMeal.class,id));

            //TODO check s.deleteUser(usermeal);
            //s.close();
            etr.commit();

        }catch (Exception e){
            System.out.println(e.getMessage());
            etr.rollback();
        }
        finally {
            currentConnection = s.disconnect();

            //s.close();
        }
        return deleted;
    }

    @Override
    public UserMeal getMeal(int id) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        if(currentConnection!=null){
            if(!s.isConnected())
            s.reconnect(currentConnection);
        }else{
            s = JPAHandlerUtil.getWorkingEntityManager().unwrap(Session.class);
        }
       UserMeal um =null;
        try{

            um = (UserMeal) em.createNamedQuery(UserMeal.BYROW).setParameter("field","id").setParameter("value",id).getResultList()
                    .stream().findFirst().orElse(null);
        }catch (Exception e){
            //log
            System.out.println(e.getMessage());
        }finally {
            currentConnection=s.disconnect();
        }
        return um;
    }

    @Override
    //TODO ну одинаковый код же с юзером, дженерик дао нужен все таки. Не повторяйте дао + аоп внедрение всех этих транзакций-манипуляций или spring data это тема.
    public boolean updateMeal(UserMeal userMeal) {
        boolean updated=false;
        Session s = emf.createEntityManager().unwrap(Session.class);
        //Session s1 = JPAHandlerUtil.buildSessionFactory().getCurrentSession();
        //todo is this session equals session retrived by unwraping em??
        System.out.println("sessions emf vs sf   s equals s1  : " +s.equals(JPAHandlerUtil.buildSessionFactory().getCurrentSession()));
        Transaction transaction = s.beginTransaction();
        try{
            transaction.begin();
            s.update(userMeal);
            transaction.commit();
            updated=true;
        }catch (Exception e ){
            transaction.rollback();
            updated =false;
            System.out.println(e.getMessage());
        }finally {
            //todo if session close then try-wuthresourceblock else disconnect ??
            Connection connection = s.disconnect();

        }

        return updated;
    }

    @Override
    public Collection<UserMeal> getByDate(Date date,int userid) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        if(currentConnection!=null){
            if(!s.isConnected())
            s.reconnect(currentConnection);
        }else{
            s = JPAHandlerUtil.getWorkingEntityManager().unwrap(Session.class);
        }
        List<UserMeal> meals = null;
        try {

            meals = em.createNamedQuery(UserMeal.BYROW).setParameter("field", "date").setParameter("value",date).getResultList();
        } catch (Exception e) {
            //log
            System.out.println(e.getMessage());
        }finally {
            currentConnection=s.disconnect();
        }
        return meals;
    }

    @Override
    public Collection<UserMeal> getByDateRange(Date start, Date end,int userid) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        if(currentConnection!=null){
            if(!s.isConnected())
            s.reconnect(currentConnection);
        }else{
            s = JPAHandlerUtil.getWorkingEntityManager().unwrap(Session.class);
        }
        List<UserMeal> meals = null;
        try {
//
        /*meals = em.createNamedQuery(UserMeal.BYRange)
                .setParameter("field","date")
                .setParameter(1,start)
                .setParameter(2,end)
                .getResultList();*/
            s.enableFilter("dateRange").setParameter("from",start).setParameter("to",end);
            meals = s.createNamedQuery(UserMeal.USERSALLORDERED).setParameter("iduser",userid).getResultList();
        } catch (Exception e) {
            //log
            System.out.println(e.getMessage());
        }finally {
           currentConnection= s.disconnect();
        }
        return meals;
    }



    @Override
    public Collection<UserMeal> getByCaloriesRange(int min, int max,int userid) {
        EntityManager em = JPAHandlerUtil.getWorkingEntityManager();
        //вот повторяющийся код в аспект нахуй вынести надо или в другой интерспетор.
        if(currentConnection!=null){
            if(!s.isConnected())
            s.reconnect(currentConnection);

        }else{
            s = JPAHandlerUtil.getWorkingEntityManager().unwrap(Session.class);
        }
        List<UserMeal> meals = null;
        try {
            meals = em.createNamedQuery(UserMeal.BYCALRange,UserMeal.class)
                    .setParameter(1,min)
                    .setParameter(2,max)
                    .setParameter("iduser",userid)
                    .getResultList();

        } catch (Exception e) {
            //log
            System.out.println(e.getMessage());
        }finally{
            currentConnection = s.disconnect();
        }

        return meals;
    }
    //TODO test it all
}
