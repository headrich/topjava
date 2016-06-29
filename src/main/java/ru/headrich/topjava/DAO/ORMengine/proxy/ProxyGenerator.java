package ru.headrich.topjava.DAO.ORMengine.proxy;

import net.sf.cglib.proxy.Enhancer;

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

/**
 * Так нам нужен такой функционал реализовать , который будет создавать прокси, реализующий наш(DAO Layer) интефррейс Entity( как это сделал бы Java Dinamic Proxy(invocationHandler)
 * Притом наследовать его от Класса сущности клиента. Как это делают байткод библиотеки типа cglib/javassist...
 * Варианты: попариться и сделать так самому. б) покопаться в доках этих библиотек, найти че-нить подобное, с) может аспектж спарвится?-может - декларируем метод, или еще че
 *AspectJ не совсем байткод манипулятор, но идеален для АОП. Спринг АОП построен на прокси, потому он канает только для выполнянения методов.
 * С другой тсороны есть удобные библы для манипулции байткодом, генерации, менять классы,создавать их и тд. Впрочем аспект тоже это могет.
 */
public class ProxyGenerator implements InvocationHandler {
    private Object proxy_clone_object;


    //тут нужен интерфейс а не класс. динамик прокси реализуют интерфейс, так же как и объект.
    //можно использовать CGLIB
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
