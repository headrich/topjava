package ru.headrich.topjava.model;

import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.ORMengine.annotations.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Montana on 07.06.2016.
 */
@Table(name = "meal")
public class UserMeal extends BaseEntity implements Serializable {
    @Column(name = "date")
    private Date date = new Date();
    @Column(name = "descr")
    private String description;
    @Column(name = "ccal")
    private Integer caloiries =3;

    public UserMeal(Date date, String description, Integer caloiries) {
        this.date = date;
        this.description = description;
        this.caloiries = caloiries;
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

    public Integer getCaloiries() {
        return caloiries;
    }

    public void setCaloiries(Integer caloiries) {
        this.caloiries = caloiries;
    }

    @Override
    public String toString() {
        return "UserMeal{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", caloiries=" + caloiries +
                '}';
    }
}
