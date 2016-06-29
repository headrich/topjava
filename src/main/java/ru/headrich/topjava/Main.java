package ru.headrich.topjava;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.ORMengine.aspects.EntityImplementator;
import ru.headrich.topjava.model.*;

import java.lang.annotation.Native;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.*;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {
    static SQLException e;
    static int a;
    static String s;
    static char c;
    static Integer I;
    static boolean b;
    static byte byt;
    @Column
    int g;
    @Native
    public String hob;
    static Logger Log = LoggerFactory.getLogger(Main.class);

    private int asp=0;
    public static void main(String[] args) throws Exception {

        System.out.format("Hello Topjava Enterprise!");

        EntityImplementator.Entity entityproxy = (EntityImplementator.Entity) Enhancer.create(User.class,new Class[]{EntityImplementator.Entity.class},new MyInvocationHandler(new User()));
        //Class[] classes = entityproxy.getClass().getInterfaces();
        //System.out.println(Arrays.toString(classes));
        //System.out.println(ClassUtils.getAllInterfaces(entityproxy.getClass())); //тут плюс ко все еще Serializable/т.е. все. c суперклассами
        //System.out.println(entityproxy.toCacheString());
        entityproxy.getId();



        //getConnection();
    /*    System.out.println(boolean.class +"  and  " + Boolean.class.getSimpleName());
        User a = new User("asd","1111e","11p",Role.ROLE_ADMIN);
        User b = new User("asd","1111e","11p",Role.ROLE_ADMIN);
        User c = new User("asd","3333e","11p",Role.ROLE_USER);
        User d = new User("asd","4444e","11p",Role.ROLE_GUEST);
        String s1 =" a";
        String s2 =" b";
        String s3 =" a";
        String s4 =" bf";
        ArrayList<User> l1= new ArrayList<>(Arrays.asList(a,d));
        ArrayList<User> l2= new ArrayList<>(Arrays.asList(b,d));
        System.out.println(new EqualsBuilder().append(l1,l2).isEquals());
        Log.info("FDFSFSFSDF");*/

        /*RDBBaseDAO rdbBaseDAO = new RDBBaseDAO() {
            @Override
            public Connection getConnection() {
                return null;
            }
        };
        try {
            rdbBaseDAO.get(new User("hf","gg","ff", Role.ROLE_ADMIN),1);
        } catch (SQLException e1) {
            System.out.print(" Невозможно отобразить. Неизвестная сущность");
            e1.printStackTrace();
        }


        try {
            getConnection();
        } catch (Exception e) {

            System.out.println("CHUCHU");
            e.printStackTrace();
            return;
        }*/

    }


    //значит entity можно пилить так.
    static class MyInvocationHandler implements MethodInterceptor{

        User u;
        public MyInvocationHandler(User ua) {
            u = ua;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            if(method.getName().contains("getLOB")){
                System.out.println("Lob is " +getLOB()+ " " +o.getClass().getName() + " :: "+ method.getName());

            }
            if(method.getName().contains("getId")){

                System.out.println("ID IS "+  getId()+ "  " +o.getClass().getName() + " :: "+ method.getName());

            }
            return null;
        }
        private   Long getId(){
            return 1L;
        };
        private  Long getLOB(){
            return 2L;
        };
    }


    public <T> int compareStates(T persist, T current){
        //reflection getting allfields
        if(persist!=null && current!=null)
            if(Collection.class.isAssignableFrom(persist.getClass()) && Collection.class.isAssignableFrom(persist.getClass())){
                return new CompareToBuilder().append(((List) persist).toArray(),((List) current).toArray()).toComparison();
            }else{
                return new CompareToBuilder().append(persist,current).toComparison();
            }
        return 0;
    }
    public static String getConnection() throws Exception {
        throw new SQLException("test");
    }
    }
