package ru.headrich.topjava.util;

import ru.headrich.topjava.model.BaseEntity;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * Created by Montana on 06.07.2016.
 */
public class JPAModifyEntityListener {



    @PostUpdate
    @PostPersist
    @PostRemove
    public void postUpdate(final BaseEntity be) {
        System.out.println("LISTENER: postUpdate: " + be.getClass());

    }
    //TODO inject that modifying to repository


}
