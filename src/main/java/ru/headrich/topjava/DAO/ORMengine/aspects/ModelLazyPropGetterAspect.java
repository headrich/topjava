package ru.headrich.topjava.DAO.ORMengine.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ru.headrich.topjava.util.Configuration;

import java.util.Collection;

/**
 * Created by Montana on 26.06.2016.
 */
@Aspect
public class ModelLazyPropGetterAspect extends ModelAspect {


    @Before("getters()")
    public void CheckLazy(){

        //???
    }



    //for lazy collections
    @AfterReturning(pointcut="getters()", returning="l")
    public void doLazyInitializing(Collection l, ProceedingJoinPoint jp){

        //if(Configuration.isLazy()){

        //определим метод, поле,
        //проверим наличие данных в кэше(StateHolder) (как?)
        // если нашли - возвращаем, если нет- загрузим значения,установим поле, вызовем гет.
        //

            Object target = jp.getTarget();
            String getterName = jp.getSignature().getName();




        //}
    }



}
