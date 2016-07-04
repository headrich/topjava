package ru.headrich.topjava.model;

import javax.persistence.*;

/**
 * Created by Montana on 07.06.2016.
 */
//неудобно в коде так с перечислением робить. Теряется весь смысл ОРМ
@Entity
@Table(name="role")
public class RoleT {
    enum role{
        ROLE_USER,
        ROLE_ADMIN,ROLE_GUEST;}

    @Id
    @Column(name = "idrole")
    long idrole;

    RoleT(long idrole) {
        this.idrole = idrole;
    }

    RoleT() {
    }
}
