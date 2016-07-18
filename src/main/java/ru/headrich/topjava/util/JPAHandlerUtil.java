package ru.headrich.topjava.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Created by Montana on 30.06.2016.
 */
public class JPAHandlerUtil {

     private static  EntityManagerFactory emf=null;
    private static  SessionFactory sessionFactory=null;

  public static EntityManagerFactory buildEntityManagerFactory(){
            if(emf==null){
                synchronized (JPAHandlerUtil.class) {
                    if(emf==null)
                    emf = Persistence.createEntityManagerFactory("topjavaRL");

                }
            }
      return emf;
  }


    public static SessionFactory buildSessionFactory(){
        //in hiber docs says that sessionfactory can be singleton? and может чето многопоточить. в отличии от сессии.
        if(sessionFactory==null){
            synchronized (JPAHandlerUtil.class){
                if(sessionFactory==null){
                    //sessionFactory= new Configuration().configure().buildSessionFactory();
                    sessionFactory = buildEntityManagerFactory().unwrap(SessionFactory.class);
                }
            }
        }

        return sessionFactory;
    }
/*
Hibernateconfig
Iterator mappingClasses = config.getClassMappings();
while(mappingClasses.hasNext()) {
   PersistentClass clazz = (PersistentClass) mappingClasses.next();
   clazz.setDynamicInsert(true);
   clazz.setDynamicUpdate(true);
}
 */


     public static EntityManager getWorkingEntityManager(){
         //получу знания - поминяю

         return emf.createEntityManager();

    }
}
