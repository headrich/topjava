package ru.headrich.topjava.DAO.ORMengine;

import ru.headrich.topjava.DAO.ORMengine.aspects.EntityImplementator;

/**
 * Created by Montana on 28.06.2016.
 */
public  class EntityImpl implements EntityImplementator.Entity {


    @Override
    public Integer getId() {
        return 1;

    }

    @Override
    public String toCacheString() {
        return "1I";
    }
}
