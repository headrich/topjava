package ru.headrich.topjava.DAO;


import ru.headrich.topjava.model.UserMeal;

import java.util.Collection;

/**
 * Created by Montana on 07.06.2016.
 */
public interface UserMealDAO {
    UserMeal getUserMeal(int id);
    boolean deleteUserMeal(int id);
    UserMeal addUserMeal(UserMeal userMeal);
    void updaateUserMeal(UserMeal userMeal);
    Collection<UserMeal> getAllUserMeals();
}
