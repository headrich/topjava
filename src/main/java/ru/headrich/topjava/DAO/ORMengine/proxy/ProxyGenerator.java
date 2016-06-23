package ru.headrich.topjava.DAO.ORMengine.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Montana on 20.06.2016.
 */

// поступим слдеующим образом. сделаем прокси, как варинат,
    // и как другой вариант реализуем клонирование объекта, и сравнения, для общеобразовательных целей
//прокси+декоратор. нужен будет для копии сущностей из базы, для отслеживания доступа к объекту и его методам , а так же сравнения с исходным объектом.
public class ProxyGenerator implements InvocationHandler {
    private Object proxy_clone_object;


    //тут нужен интерфейс а не класс. динамик прокси реализуют интерфейс, так же как и объект.
    public static <T> Object generateProxy(T object){

        return Proxy.newProxyInstance(T.getClass().getClassLoader(), null, new ProxyGenerator(T));

    }

    public ProxyGenerator(Object proxy_clone_object) {
        this.proxy_clone_object = proxy_clone_object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }
}
