package ru.headrich.topjava.DAO.ORMengine;

import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.ORMengine.annotations.Table;
import ru.headrich.topjava.model.BaseEntity;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Montana on 13.06.2016.
 */
public class MappingUtil {

    //пока войд, но потом суперсущность

    public static Map<String,List<String>> gettingMap(Object obj) throws Exception {

        //зырим поля , по ним разбиваем резалтсет(GetInt getString итд
        //затем с аннотациями, сморим какие поля чем аннотированы, и делаем соответвующие вещи.
        //будет достаточно Table Column
        //ну и тогда конфиг , заранее сканируем и создаем сущности, прокси

        //отыскиваем  аннотированные или сконфигурированные ОРМ-сущности
        //для того чтобы было проще добавить сущность, и выдавать ошибку(исключения) если
        //запрос не верный, т.е на получение левого незамапленного объекта.
        Map<String,List<String>> mappedTables = null;
        if(obj.getClass().isAnnotationPresent(Table.class)) {

            mappedTables = new HashMap<>(1);//1 таблица 1 набор!!!!!!!! полей
            List<String> columnNames = new LinkedList<>();
            mappedTables.put(!obj.getClass().getAnnotation(Table.class).name().equals(" ") ?
                    obj.getClass().getAnnotation(Table.class).name() : obj.getClass().getSimpleName().toLowerCase(),columnNames);
            int cnt =0;
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    cnt++;
                    columnNames.add(!field.getAnnotation(Column.class).name().equals(" ")? field.getAnnotation(Column.class).name(): field.getName());

                }
            }
            if(cnt==0)columnNames.addAll( (List<String>)Arrays.asList(obj.getClass().getDeclaredFields()).stream().map(value -> value.getName()));
        }else throw new Exception("not defined entity");

        return mappedTables;

    }


}
