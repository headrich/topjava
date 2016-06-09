package ru.headrich.topjava.service;


import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Created by Montana on 07.06.2016.
 */
public interface UserMealService {

    UserMeal addMeal(UserMeal userMeal);

    List<UserMeal> getAllMeals();

    void deleteMeal(int id) throws NotFoundException;

    UserMeal get(int id) throws NotFoundException;

    List<UserMeal> getByDate(Date date);

    List<UserMeal> getByDateRange(Date start, Date end);

    List<UserMeal> getByCalories(int calories);

    List<UserMeal> getByCaloriesRange(int min,int max);

}
