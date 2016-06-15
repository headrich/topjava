package ru.headrich.topjava.model;

import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.ORMengine.annotations.Table;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Montana on 07.06.2016.
 */
@Table(name = "user")
public class User extends NamedEntity implements Serializable {
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "registered")
    private Date registered=new Date();
    @Column(name = "enabled")
    private boolean enabled =true;
    private Set<Role> authorities;

    private boolean logged = false;



    public User(String name, String email, String password,Role role, Role... roles) {
        super(name);
        this.email = email;
        this.password = password;
        this.enabled=true;
        this.authorities = EnumSet.of(role,roles);
    }

    public User(String name) {
        super(name);
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
    @Transient
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
