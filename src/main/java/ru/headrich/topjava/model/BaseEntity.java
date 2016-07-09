package ru.headrich.topjava.model;

import ru.headrich.topjava.util.JPAModifyEntityListener;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by Montana on 07.06.2016.
 */

@MappedSuperclass
@Access(value = AccessType.PROPERTY)
//@EntityListeners(JPAModifyEntityListener.class)//или же динамически добавить аннотацию всем замапленным
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    protected Integer id;


    public BaseEntity() {
    }

    @Transient
    public boolean isNew(){
        return(this.id ==null);
    }

    protected Date lastUpdateDate = new Date();

    @Transient
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }



    @PostUpdate
    @PostPersist
    @PostRemove
    protected void markModified(){
        System.out.println("JPA  POST EVENTS  FROM ENTITY");
        this.lastUpdateDate = new Date();

    }

    //если мы обновляем юзера, тогда это отразится тут, зачем мне знать изменения в репозитории в целом, если мне нужно словить обновления конечного пользователя
    //который super.this
}
