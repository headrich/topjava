package ru.headrich.topjava;

import ru.headrich.topjava.DAO.DAOFactory;
import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.RDB.RDBBaseDAO;
import ru.headrich.topjava.model.*;

import javax.naming.Context;
import java.lang.annotation.Annotation;
import java.lang.annotation.Native;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

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

    public static void main(String[] args)  {

        RDBBaseDAO rdbBaseDAO = new RDBBaseDAO() {
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
        }
        System.out.format("Hello Topjava Enterprise!");
    }
    public static String getConnection() throws Exception {
        throw new SQLException("test");
    }
    }
