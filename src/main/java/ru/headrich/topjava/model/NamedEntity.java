
package ru.headrich.topjava.model;

import javax.persistence.*;

/**
 * Created by Montana on 07.06.2016.
 */
@MappedSuperclass
@Access(value = AccessType.PROPERTY)
public class NamedEntity extends BaseEntity {

    protected String name;

    public NamedEntity(String name) {
        this.name = name;
    }
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
