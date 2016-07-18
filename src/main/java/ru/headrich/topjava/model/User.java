package ru.headrich.topjava.model;

import org.hibernate.annotations.*;
import ru.headrich.topjava.util.converters.BooleanToIntegerConverter;
import ru.headrich.topjava.util.converters.PasswordConverter;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.*;

import static ru.headrich.topjava.model.User.ALL;

/**
 * Created by Montana on 07.06.2016.
 */
//TODO CHANGE TO LOCALDATE ALL DATES (ON DOSUG)

@Access(value = AccessType.FIELD)
@Entity
//моя фича по обновлению только измененных полей - http://www.mkyong.com/hibernate/hibernate-dynamic-update-attribute-example/
// , как реализована хз,либо через встраивание байткода либо через лругие прокси, либо хиберовские внутренние механизмы событий и слушателей ,
// типа онфлушдерти и сверка значений полей.
@DynamicUpdate //hiber api to upadte only modified columns
@Table(name="user")
@AttributeOverride(name = "id",column = @Column(name = "iduser"))
@NamedNativeQuery(name = User.ByEmail,query = "SELECT * from user u where u.email=:email",resultClass = User.class,
        hints =@QueryHint(name = "javax.persistence.query.timeout",value = "5000") )
@NamedQueries({
        @NamedQuery(name = User.ByName, query = "select u from User u where u.name=:name"),
        @NamedQuery(name = User.DELETE,query ="delete from User u where u.id=:id"),
        @NamedQuery(name=User.ALL,query = "select u from User u",hints = {@QueryHint(name = "javax.persistence.loadgraph",value="userRoleGraph")})
})
@NamedEntityGraph(name = "userRoleGraph", attributeNodes = @NamedAttributeNode("authorities"))
@FetchProfiles({
        @FetchProfile(name = "userMealsFP",fetchOverrides = @FetchProfile.FetchOverride(entity = User.class,association = "meals",mode = FetchMode.JOIN)),
        @FetchProfile(name = "userRolesFP",fetchOverrides = @FetchProfile.FetchOverride(entity = User.class,association = "authorities",mode = FetchMode.JOIN))
})


//@NamedEntityGraph - именные графы https://docs.oracle.com/javaee/7/tutorial/persistence-entitygraphs002.htm#BABFIGEI
//  пример с  подграфами и http://www.thoughts-on-java.org/jpa-21-entity-graph-part-2-define/
//в хибернейте для этого есть @FetchProfile http://www.concretepage.com/hibernate/fetchprofile_hibernate_annotation -
// фетчмод subselect -аналог subgrpah в EG - подтягивать связные записи для дочерних коллекций.
// еще есть @Fetch c FetchMode , устанавливает мод подгрузки коллекций http://www.mkyong.com/hibernate/hibernate-fetching-strategies-examples/ или http://shengwangi.blogspot.ru/2015/05/all-about-hibernate-fetch-when-and-how.html
//но профили и графы круче, их можно включить-выключить.
@FilterDef(name = "mealsDateFilter", parameters = @ParamDef(name = "mealsDateFilterParam",type = "date"),defaultCondition = "date ==:mealsDateFilter")
//фильры хорошо определят в хмл, написал, стер и рад. В дао лезть не надо, код править
@FilterDefs({
        @FilterDef( name="mealsCaloriesRange",parameters={@ParamDef(name="min",type="integer"),@ParamDef(name="max",type="integer")}
                ,defaultCondition = "ccal between :min and :max"),
        @FilterDef( name="mealsCaloriesRangeUnder",parameters = @ParamDef(name="ccal",type="integer")),
        @FilterDef( name="mealsCaloriesRangeOver",parameters = @ParamDef(name="ccal",type="integer")),
        @FilterDef(name="mealsDateRange",parameters={@ParamDef(name="from",type="date"),@ParamDef(name="to",type="date")})
})

public class User extends NamedEntity {

    public static final String ALL = "User.getAll";
    public static final String DELETE = "User.deleteOne";
    public static final String ByEmail = "User.getByEmail";
    public static final String ByName = "User.getByName";
    //@Basic выставляется по умолчанию всем, потмоу есил нужно че-то не сохранять- @Transient
// @Basic используется для основных типов(которые поддерживают различные бд).есть табличка. @Basic собирает имя атрибута
// By default all Basic mappings are EAGER. Можно устанвоить Lazy
// /@Column для того чтобы установить специфичное имя и параметры типа для бд
    //@Basic // так что можно не использвать эту аннотацию.
    private String email;
    @Basic
    @Convert(converter = PasswordConverter.class)
    private String password;
    @Basic
    private String salt;

    @Basic
    //@Convert(converter = ru.headrich.topjava.util.converters.MysqlDateConverter.class)
    //У JPA есть  @Temporal для маппинга типа даты для бд  из например java.util.date/calendar (в sql.DATE,TIME,) или даже из sql.date в timestamp
    @Temporal(TemporalType.DATE)
    private Date registered=new Date();
    @Basic
    @Convert(converter = BooleanToIntegerConverter.class)//такие конвертеры наверно встроены во многие провайдеры ЖПА
    private boolean enabled =true;



   /* @ManyToMany
    @JoinTable(name = "authority", joinColumns = {@JoinColumn(name = "user",referencedColumnName = "iduser")},
            inverseJoinColumns = {@JoinColumn(name = "authority",referencedColumnName = "idrole")})
    @Enumerated*/
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "authority")
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    //@Basic(fetch = FetchType.LAZY)
    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name="authority",joinColumns = @JoinColumn(name = "user"))
    private Set<Role> authorities;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    //сюда можно фильтер прикрутить. это не jointablefilter

    @Filters({@Filter(name="mealsCaloriesRangeUnder" ,condition="ccal <= :ccal"),
            @Filter(name="mealsCaloriesRangeOver",condition="ccal >= :ccal"), @Filter(name="dateRange",condition=" date between :from and :to ")
            ,@Filter(name="mealsCaloriesRange",condition= "ccal between :min and :max"),
            @Filter(name = "mealsDateFilter", condition="date >= :mealsDateFilterParam"),

    })
    private List<UserMeal> meals = new ArrayList<>();


    @Transient
    private boolean logged = false;


    public User() {
        super();

    }

    public User(String name, String email, String password, Role role, Role... roles) {
        super(name);
        this.email = email;
        this.password = password;
        this.enabled=true;
        this.authorities = EnumSet.of(role,roles);

    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public List<UserMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<UserMeal> meals) {
        this.meals = meals;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



    public void addAuthorities(Role authoritiy) {
       if(authorities==null){
           authorities=EnumSet.of(authoritiy);
       }else{
           authorities.add(authoritiy);
       }
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registered=" + registered +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                '}';
    }
}
