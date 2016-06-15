package ru.headrich.topjava.DAO.ORMengine.annotations;

import java.lang.annotation.*;

/**
 * Created by Montana on 13.06.2016.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE,ElementType.CONSTRUCTOR})
public @interface Table {
String name() default "";

}
