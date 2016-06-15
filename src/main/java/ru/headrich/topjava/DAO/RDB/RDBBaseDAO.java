package ru.headrich.topjava.DAO.RDB;

import ru.headrich.topjava.DAO.ORMengine.MappingUtil;
import ru.headrich.topjava.model.BaseEntity;

import java.sql.*;
import java.util.*;

/**
 * Created by Montana on 11.06.2016.
 */
public abstract class RDBBaseDAO {
    protected Connection connection;
    private static  String jdbcDriver = "com.mysql.jdbc.Driver";
    private String url;
    private String username;
    private String password;

    private Properties conProps;


    public abstract Connection getConnection();

    //тут что-то с дженериками можно сделать
    //типа передаём сюда сущность, которую хотим получить или прост
    //так и сдлелаю
    public void createQuery(String query) throws SQLException {

        PreparedStatement ps = (PreparedStatement) connection.createStatement();
        ResultSet rs  = ps.executeQuery(query);
        while(rs.next()){

        }
    }


    //тут возникает весьма трудоемкая задача, потому как
    //запилим аннотации и маппинг. и зырить кикие поля сущности в таблице
    public BaseEntity get(Object be,int id) throws SQLException {
        try {
            //лучше сканируем заранее все , затем тут проверяем контейнс
            //List<String> tableFields = MappingUtil.gettingMap(be);
            Map<String,List<String>> mappedTables = MappingUtil.gettingMap(be);

            mappedTables.forEach((k,v)->System.out.println("Table : " + k + " Columns : " + v));

            PreparedStatement ps = (PreparedStatement) connection.createStatement();
            ResultSet rs  = ps.executeQuery("SELECT "+ "столбцы"+" FROM" + "имя таблица");
            while(rs.next()){
                BaseEntity.class.getDeclaredFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }



    public BaseEntity save(BaseEntity be){
        return null;
    }

    public List<? extends BaseEntity> list(String query){
        return null;
    }

}
