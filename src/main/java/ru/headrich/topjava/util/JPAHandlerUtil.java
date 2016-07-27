package ru.headrich.topjava.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Name;


/**
 * Created by Montana on 30.06.2016.
 */
@org.springframework.context.annotation.Configuration
public class JPAHandlerUtil  {

     private static  EntityManagerFactory emf=null;
    private static  SessionFactory sessionFactory=null;

    //@Bean(name = "emf") // это спринг // спринг - по сути это провайдер некторых java спек .
    //@Produces //это в спецификации cdi jsr330
    @Primary // = @Alternative in cdi
    @Bean(name = "emf")
  public static EntityManagerFactory buildEntityManagerFactory(){
            if(emf==null){
                synchronized (JPAHandlerUtil.class) {
                    if(emf==null)
                    emf = Persistence.createEntityManagerFactory("topjava");

                }
            }
      return emf;
  }

    //@Bean(name = "sf")
    @Bean(name = "sf")
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
