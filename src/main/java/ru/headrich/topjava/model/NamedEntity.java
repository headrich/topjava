
package ru.headrich.topjava.model;

/**
 * Created by Montana on 07.06.2016.
 */
public class NamedEntity extends BaseEntity {
    protected String name;

    public NamedEntity(String name) {
        this.name = name;
    }

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
