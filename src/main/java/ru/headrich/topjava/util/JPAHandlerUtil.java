package ru.headrich.topjava.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Name;


/**
 * Created by Montana on 30.06.2016.
 */
@Component
public class JPAHandlerUtil {

     private static  EntityManagerFactory emf=null;
    private static  SessionFactory sessionFactory=null;

    @Bean
  public static EntityManagerFactory buildEntityManagerFactory(){
            if(emf==null){
                synchronized (JPAHandlerUtil.class) {
                    if(emf==null)
                    emf = Persistence.createEntityManagerFactory("topjava");

                }
            }
      return emf;
  }

    @Bean
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
