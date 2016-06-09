package ru.headrich.topjava.model;

import java.util.Date;

/**
 * Created by Montana on 07.06.2016.
 */
public class UserMeal extends BaseEntity {
    private Date date = new Date();
    private String description;
    private Integer caloiries;

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
