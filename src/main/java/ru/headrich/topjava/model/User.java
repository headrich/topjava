package ru.headrich.topjava.model;

import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.ORMengine.annotations.Table;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.sql.Date;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Montana on 07.06.2016.
 */
@Table(name = "user")
public class User extends NamedEntity implements Serializable {
   /* @Column(name = "iduser")
    private Integer id;
    @Column(name = "name")
    private String name;*/
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "registered")
    private Date registered = Date.valueOf(LocalDate.now());
    @Column(name = "enabled")
    private boolean enabled =true;

    private Set<Role> authorities;

    private List<UserMeal> meals;

    private boolean logged = false;



    public User(String name, String email, String password,Role role, Role... roles) {
        super(name);
        this.email = email;
        this.password = password;
        this.enabled=true;
        this.authorities = EnumSet.of(role,roles);

    }

    public User(){
        super();

    }

    public User(String name) {
        super(name);
    }

     public String getEmail() {
        return email;
    }


    @MAT
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public void addAuthorities(Role authoritiy) {
       if(authorities==null){
           authorities=EnumSet.of(authoritiy);
       }else{
           authorities.add(authoritiy);
       }
    }
    public void setBorsch(){
        System.out.println("SET BORSCH");
    }
    public String getBorsch(){
        System.out.println("GET BORSCH");
        return "GET BORSCH";
    }
    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }
    @Transient
    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public List<UserMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<UserMeal> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='"+id+'\''+
                ",name='"+name+'\''+
                ",email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registered=" + registered +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (enabled != user.enabled) return false;
        if (logged != user.logged) return false;
        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        if (!registered.equals(user.registered)) return false;
        if (!authorities.equals(user.authorities)) return false;
        return meals != null ? meals.equals(user.meals) : user.meals == null;

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + registered.hashCode();
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + authorities.hashCode();
        result = 31 * result + (meals != null ? meals.hashCode() : 0);
        result = 31 * result + (logged ? 1 : 0);
        return result;
    }
}
