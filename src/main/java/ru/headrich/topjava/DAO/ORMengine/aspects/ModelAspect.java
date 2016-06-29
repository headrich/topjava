package ru.headrich.topjava.DAO.ORMengine.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Montana on 26.06.2016.
 */
@Aspect
public abstract class ModelAspect {

    @Pointcut("(execution(void ru.headrich.topjava.model..*.set*(..)) || execution(void ru.headrich.topjava.model..*.add*(..))) && @args(*,..)")
    public void setters(){}
    //можно как то добавить к model..*. что нить отдельно?
    @Pointcut("execution(void ru.headrich.topjava.model..*.get*())")
    public void getters(){}
}
