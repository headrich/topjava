package ru.headrich.topjava.DAO.RDB.usermealdao;

import ru.headrich.topjava.DAO.UserMealDAO;
import ru.headrich.topjava.model.UserMeal;

import java.util.Collection;
import java.util.List;

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
    public UserMeal saveUserMeal(UserMeal userMeal) {
        return null;
    }

    @Override
    public void updateUserMeal(UserMeal userMeal) {

    }

    @Override
    public Collection<UserMeal> getAllUserMeals() {
        return null;
    }

    @Override
    public Collection<UserMeal> listByUser(int iduser) {
        return null;
    }

    @Override
    public void SaveOrUpdateMeal(UserMeal meal) {

    }

    @Override
    public UserMeal getByField(String field) {
        return null;
    }

    @Override
    public List<UserMeal> listByField(String field) {
        return null;
    }

    //
}
