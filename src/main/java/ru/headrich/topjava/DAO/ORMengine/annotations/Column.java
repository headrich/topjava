package ru.headrich.topjava.DAO.ORMengine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;

/**
 * Created by Montana on 12.06.2016.
 */
@Retention(RetentionPolicy.RUNTIME) //для использования annotation в рантайме
@Target(value = {ElementType.FIELD,ElementType.METHOD})
public @interface Column {

    String name() default "";

}
