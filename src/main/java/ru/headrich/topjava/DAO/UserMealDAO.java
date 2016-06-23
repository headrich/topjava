package ru.headrich.topjava.DAO;


import ru.headrich.topjava.model.UserMeal;

import java.util.Collection;
import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */
public interface UserMealDAO {

    UserMeal getUserMeal(int id);
    boolean deleteUserMeal(int id);
    UserMeal saveUserMeal(UserMeal userMeal);
    void updateUserMeal(UserMeal userMeal);
    Collection<UserMeal> getAllUserMeals();
    Collection<UserMeal> listByUser(int iduser);
    void SaveOrUpdateMeal(UserMeal meal);
    UserMeal getByField(String field);
    List<UserMeal> listByField(String field);

}
