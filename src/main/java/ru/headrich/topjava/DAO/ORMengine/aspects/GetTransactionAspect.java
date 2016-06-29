package ru.headrich.topjava.DAO.ORMengine.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Montana on 25.06.2016.
 */
@Aspect
public class GetTransactionAspect {

@Pointcut("execution(public * ru.headrich.topjava.DAO.UserDAO.*(..)) || execution(public * ru.headrich.topjava.DAO.UserMealDAO.*(..))")
    public void daoMethod(){}

    @Around("daoMethod()")
    public Object connectionControlAdvice(ProceedingJoinPoint pjp) throws Throwable {

        return pjp.proceed();
    }


    /*
    Возможно стоит сделать совет, который будет подбирать названия столбцов для запроса , обновления?
     */
}
