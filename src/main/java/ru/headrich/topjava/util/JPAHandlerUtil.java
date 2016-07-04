package ru.headrich.topjava.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Montana on 30.06.2016.
 */
public class JPAHandlerUtil {

     private static  EntityManagerFactory emf=null;

  public static EntityManagerFactory buildEntityManagerFactory(){
            if(emf==null){
                synchronized (JPAHandlerUtil.class) {
                    if(emf==null)
                    emf = Persistence.createEntityManagerFactory("topjava");
                }
            }
      return emf;
  }



     public static EntityManager getWorkingEntityManager(){
         //получу знания - поминяю

         return emf.createEntityManager();

    }
}
