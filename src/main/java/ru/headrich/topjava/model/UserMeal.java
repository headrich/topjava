package ru.headrich.topjava.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Montana on 07.06.2016.
 */
@Access(value = AccessType.FIELD)
@Entity
@Table(name = "meal")
@AttributeOverride(name = "id",column = @Column(name = "idmeal"))
//фильтр на range будет актуален тут. т.к получив напирмер сегодняшине, можно получить и по калоирйности их не усложняя запросы, а просто включая фильтр.
@NamedQueries(value = {
        @NamedQuery(name=UserMeal.All,query = "select m from UserMeal as m"),
        @NamedQuery(name=UserMeal.USERSALLORDERED,query = "select um from UserMeal as um where um.user.id = :iduser order by um.date desc"),
        @NamedQuery(name=UserMeal.DELETE,query = "delete from UserMeal um where um.id = :id"),
        @NamedQuery( name = UserMeal.BYROW,query = "select um from UserMeal as um  where :field=:value"),//:field не робит(
        @NamedQuery(name=UserMeal.BYCALRange,query = "select um from UserMeal as um where  um.user.id=:iduser and um.calories between ?1 and ?2 ")})
//В хибернейт апи , для таких запросов  есть @Filter и @FilterDef(определяет парамтеры для фильтра).
//а также @FilterJoinTable для подтягивания из бд отфильтрованных значений в поле сущности.
// @FilterJoinTable is used with @JoinTable. When entity is loaded, collection will be filtered according to @FilterJoinTable definition
/*
@FilterDef(name="crange",parameters={@ParamDef(name="mincal",type="integer"),@ParamDef(name="maxcal",type="integer",@ParamDef(name="ccal",type="integer")})
@Filters({@Filter(name="crange",condition=" ccal between :mincal and :maxcal "),@Filter(name="crange" ,condition="ccal <= :ccal")})
//затем в коде Filter crange = session.enableFilter("crange"); crange.setParameter("mincal",100); crange.setParameter("maxcal",500); crange.setParameter("ccal",250);
// и фильтруем все запросы из бд. А как надоест session.disableFilter("crange);
//http://www.concretepage.com/hibernate/hibernate-filter-and-filterjointable-annotation-example
 */

@FilterDefs({
        @FilterDef( name="calRange",parameters={@ParamDef(name="min",type="integer"),@ParamDef(name="max",type="integer")}
                ,defaultCondition = "ccal between :min and :max"),
        @FilterDef( name="calRangeUnder",parameters = @ParamDef(name="ccal",type="integer")),
        @FilterDef( name="calRangeOver",parameters = @ParamDef(name="ccal",type="integer")),
        @FilterDef(name="dateRange",parameters={@ParamDef(name="from",type="date"),@ParamDef(name="to",type="date")})
})
@Filters({@Filter(name="calRangeUnder" ,condition="ccal <= :ccal"),
        @Filter(name="calRangeOver",condition="ccal >= :ccal"), @Filter(name="dateRange",condition=" date between :from and :to ")
        ,@Filter(name="calRange",condition= "ccal between :min and :max")
})


public class UserMeal extends BaseEntity {
    public static final String BYROW = "UserMeal.getByRow";
    public static final String BYCALRange = "UserMeal.getCaloriesByRange";
    public static final String All = "UserMeal.getAllMeals";
    public static final String USERSALLORDERED = "UserMeal.getAllUserMeals";
    public static final String DELETE = "UserMeal.delete";







    //@Convert(converter = ru.headrich.topjava.util.converters.MysqlDateConverter.class)
    @Temporal(value = TemporalType.TIMESTAMP)
    @Basic
    private Date date = new Date();
    @Column(name = "descr")
    private String description;
    @Column(name = "ccal")
    private Integer calories;


    @ManyToOne(fetch = FetchType.LAZY)
    //@Id
    @JoinColumn(name="user",referencedColumnName = "iduser")
    //JoinTable когда связть через таблицу отношений(третья таблица с ключами), JoinColumn - связь по ключу.
    private User user;

    public UserMeal(Date date, String description, Integer caloiries) {
        this.date = date;
        this.description = description;
        this.calories = caloiries;
    }

    public UserMeal() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserMeal{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", caloiries=" + calories +
                '}';
    }
}
