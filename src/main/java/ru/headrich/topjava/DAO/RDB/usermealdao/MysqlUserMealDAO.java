package ru.headrich.topjava.DAO.RDB.usermealdao;

import ru.headrich.topjava.DAO.UserMealDAO;
import ru.headrich.topjava.model.UserMeal;

import java.util.Collection;

/**
 * Created by Montana on 11.06.2016.
 */
public class MysqlUserMealDAO implements UserMealDAO{
    @Override
    public UserMeal getUserMeal(int id) {
        return null;
    }

    @Override
    public boolean deleteUserMeal(int id) {
        return false;
    }

    @Override
    public UserMeal addUserMeal(UserMeal userMeal) {
        return null;
    }

    @Override
    public void updaateUserMeal(UserMeal userMeal) {

    }

    @Override
    public Collection<UserMeal> getAllUserMeals() {
        return null;
    }

    //
}
