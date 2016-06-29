package ru.headrich.topjava.DAO.ORMengine.aspects;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import ru.headrich.topjava.model.User;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

/**
 * Created by Montana on 28.06.2016.
 */

@Aspect
public class EntityImplementator {

    //что-то свзанное с конкретной сессией нужно, с изменениями, с доступом к объекту... такс
    public interface Entity{
        Integer getId();
        String toCacheString();
    }

    @DeclareParents(value="ru.headrich.topjava.model.*",defaultImpl = ru.headrich.topjava.DAO.ORMengine.EntityImpl.class )
    private Entity implemetsEntity;



    @Pointcut("initialization(ru.headrich.topjava.model.*.new(..))")
    protected void modelConstruct(){}

    @After("modelConstruct()")
    public void proxyGen(JoinPoint jp){
            //System.out.println(jp.getTarget().getClass() + jp.getSignature().toString() + ClassUtils.getAllInterfaces(jp.getTarget().getClass()));

    }

    @Around("call(String Entity.toCacheString(..))")
    public Object buildCacheString(ProceedingJoinPoint jp) throws Throwable {
        Object target = jp.getTarget();
        System.out.println(Arrays.toString(PropertyUtils.getPropertyDescriptors(target)));
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(target);
        StringBuilder sb = new StringBuilder();
        for(PropertyDescriptor pd: pds){
            sb.append(pd.getName()).append(':').append(PropertyUtils.getProperty(target,pd.getName())).append(";\n");
        }
        //тут у нас геттер используется, но это фигово потом у что они у нас засовечены, нужно значит отключать совет на время. или *
        //можно и в ДЖСОН перепилить.

        return sb.toString();
    }

    public static void main(String[] args) {
        User u = new User();
        Entity e = (Entity)u;
        System.out.println(e.toCacheString());
    }

}
