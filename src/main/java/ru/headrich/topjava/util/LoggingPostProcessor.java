package ru.headrich.topjava.util;

import  java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.headrich.topjava.util.annotations.Logging;

/**
 * Created by Montana on 22.07.2016.
 */
@Component
public class LoggingPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {

        Class beanClass = o.getClass();

        ReflectionUtils.doWithFields(beanClass,field -> {
            field.setAccessible(true);
            field.set(o, LoggerFactory.getLogger(beanClass));
        },field ->
            field.isAnnotationPresent(Logging.class) && field.getClass().equals(Logger.class)
        );

        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
