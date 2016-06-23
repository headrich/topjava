package ru.headrich.topjava.model;

import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.ORMengine.annotations.Table;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Montana on 07.06.2016.
 */
@Table(name = "meal")
public class UserMeal extends BaseEntity implements Serializable {
    @Column(name = "date")
    private Date date;
    @Column(name = "descr")
    private String description;
    @Column(name = "ccal")
    private Integer caloiries;

    @Column(name = "user")
    private User user;


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserMeal{" +
                "id='"+id+'\''+
                ",date=" + date +
                ", description='" + description + '\'' +
                ", caloiries=" + caloiries +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserMeal userMeal = (UserMeal) o;

        if (date != null ? !date.equals(userMeal.date) : userMeal.date != null) return false;
        if (description != null ? !description.equals(userMeal.description) : userMeal.description != null)
            return false;
        if (caloiries != null ? !caloiries.equals(userMeal.caloiries) : userMeal.caloiries != null) return false;
        return user != null ? user.equals(userMeal.user) : userMeal.user == null;

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (caloiries != null ? caloiries.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
