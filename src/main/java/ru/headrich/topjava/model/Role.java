package ru.headrich.topjava.model;

import ru.headrich.topjava.DAO.ORMengine.annotations.Table;

/**
 * Created by Montana on 07.06.2016.
 */
@Table(name = "role")
public enum Role {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_GUEST
}
