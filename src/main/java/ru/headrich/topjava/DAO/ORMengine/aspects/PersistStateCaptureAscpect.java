package ru.headrich.topjava.DAO.ORMengine.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import ru.headrich.topjava.DAO.UserDAOImpl;

/**
 * Created by Montana on 26.06.2016.
 */
@Aspect
public class PersistStateCaptureAscpect {


    @Pointcut("execution(* ru.headrich.topjava.DAO..*.get*(..))")
    private void daoGetter(){}

    @Pointcut("execution(* ru.headrich.topjava.DAO..*.list*(..))")
    private void daoList(){}

    @Pointcut("execution(!void ru.headrich.topjava.DAO..*.create*(..)) ") //&& returning some resultset
    private void daoQuery(){}

    @Pointcut("execution(* ru.headrich.topjava.DAO..*.save*(..))")
    private void daoSave(){}


    /**
     * Проверим не кэшированны ли результаты, если да, то возвращаем их, если нет, то выполняем запросы к бд.
     * Все должно быть в рамках сессии. Если сессия закрыта то и кэша нет.
     * @param returned
     * @param pjp
     * @return
     */
    protected Object checkCashedResults(Object returned, ProceedingJoinPoint pjp){
        /*
        if(cashe. contains ) returned = cashe.getit()
        else pjp.proceed()

        */
        return null;
    }

    /**
     * Если загрузили из базы, то добавляем в кэш.
     * @param returned
     * @return
     * @throws Throwable
     */
    protected Object capturePersistents(Object returned) throws Throwable {
        //if(!cahched)
        //save returned to cahche StаteHolder

        return null;

    }

    @AfterReturning(pointcut="daoGetter() || daoList() || daoQuery() || daoSave()", returning="returned")
    public void managePersistents(Object returned, JoinPoint jp) throws Throwable {

        Object res = checkCashedResults(returned,null);
        capturePersistents(res);

    }

}
