package ru.headrich.topjava.model;

import javax.persistence.*;


/**
 * Created by Montana on 07.06.2016.
 */

@MappedSuperclass
@Access(value = AccessType.PROPERTY)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    protected Integer id;
    @Transient
    public boolean isNew(){
        return(this.id ==null);
    }
}
