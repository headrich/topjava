package ru.headrich.topjava.service;

import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.UserMealRepository;
import ru.headrich.topjava.util.exception.NotFoundException;

import java.util.Date;
import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */
public class UserMealServiceImpl implements UserMealService {

    private UserMealRepository userMealRepository;

    public void setUserMealRepository(UserMealRepository userMealRepository) {
        this.userMealRepository = userMealRepository;
    }

    @Override
    public UserMeal addMeal(UserMeal userMeal) {
        return null;
    }

    @Override
    public List<UserMeal> getAllMeals() {
        return null;
    }

    @Override
    public void deleteMeal(int id) throws NotFoundException {

    }

    @Override
    public UserMeal get(int id) throws NotFoundException {
        return null;
    }

    @Override
    public List<UserMeal> getByDate(Date date) {
        return null;
    }

    @Override
    public List<UserMeal> getByDateRange(Date start, Date end) {
        return null;
    }

    @Override
    public List<UserMeal> getByCalories(int calories) {
        return null;
    }

    @Override
    public List<UserMeal> getByCaloriesRange(int min, int max) {
        return null;
    }
}
