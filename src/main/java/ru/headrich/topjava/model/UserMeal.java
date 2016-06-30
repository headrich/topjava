package ru.headrich.topjava.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Montana on 07.06.2016.
 */
@Entity
@Table(name = "meal")
@AttributeOverride(name = "id",column = @Column(name = "iduser"))
public class UserMeal extends BaseEntity {
    @Convert(converter = ru.headrich.topjava.util.converters.MysqlDateConverter.class)
    @Basic
    private Date date = new Date();
    @Column(name = "descr")
    private String description;
    @Column(name = "ccal")
    private Integer caloiries;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private User user;

    public UserMeal(Date date, String description, Integer caloiries) {
        this.date = date;
        this.description = description;
        this.caloiries = caloiries;
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
