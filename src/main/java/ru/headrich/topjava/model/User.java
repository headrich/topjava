package ru.headrich.topjava.model;

import ru.headrich.topjava.util.converters.BooleanToIntegerConverter;
import ru.headrich.topjava.util.converters.PasswordConverter;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Montana on 07.06.2016.
 */



@Entity
@Table(name="user")
@AttributeOverride(name = "id",column = @Column(name = "iduser"))
public class User extends NamedEntity {



    @Basic
    private String email;
    @Basic
    @Convert(converter = PasswordConverter.class)
    private String password;
    @Basic
    private String salt;
    @Basic
    @Convert(converter = ru.headrich.topjava.util.converters.MysqlDateConverter.class)
    private Date registered=new Date();
    @Basic
    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean enabled =true;

    @ManyToMany(mappedBy = "user")
    @JoinTable(
            name = "authority",
            joinColumns = @JoinColumn(name="user",referencedColumnName = "iduser"),
            inverseJoinColumns = @JoinColumn(name = "authority",referencedColumnName = "idrole")
            )
    private Set<Role> authorities;
    @OneToMany(mappedBy = "user")
    private List<UserMeal> meals;


    @Transient
    private boolean logged = false;



    public User(String name, String email, String password,Role role, Role... roles) {
        super(name);
        this.email = email;
        this.password = password;
        this.enabled=true;
        this.authorities = EnumSet.of(role,roles);

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

    public Collection<Role> getAuthorities() {
        return authorities;
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
