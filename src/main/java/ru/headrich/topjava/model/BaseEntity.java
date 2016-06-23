package ru.headrich.topjava.model;

import java.io.Serializable;

/**
 * Created by Montana on 07.06.2016.
 */
public class BaseEntity implements Serializable{
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    protected Integer id;

    public boolean isNew(){
        return(this.id ==null);
    }
}
