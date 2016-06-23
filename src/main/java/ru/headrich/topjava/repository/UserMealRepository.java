package ru.headrich.topjava.repository;

import ru.headrich.topjava.model.UserMeal;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */
public interface UserMealRepository {

    UserMeal addMeal(UserMeal userMeal);

    List<UserMeal> getAllMeals();

    boolean deleteMeal(int id);

    UserMeal getMeal(int id);

    void updateMeal(UserMeal userMeal);

    Collection<UserMeal> getByDate(Date date);

    Collection<UserMeal> getByDateRange(Date start, Date end);

    Collection<UserMeal> getByCalories(int calories);

    Collection<UserMeal> getByCaloriesRange(int min,int max);

    Collection<UserMeal> listAllUserMeals(int userid);


}
