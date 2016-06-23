package ru.headrich.topjava.repository;

import ru.headrich.topjava.DAO.UserMealDAO;
import ru.headrich.topjava.model.UserMeal;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Montana on 22.06.2016.
 */
public class UserMealRepositoryImpl implements UserMealRepository {

    UserMealDAO mealDao;

    //тут у нас вроде как срабатывает инъекция зависимости(ДАО) через конструктор.
    //можно было бы через сеттер, но так как репозиторий нас не может без ДАО...

    public UserMealRepositoryImpl(UserMealDAO dao) {
        this.mealDao = dao;
    }

    @Override
    public UserMeal addMeal(UserMeal userMeal) {
        return mealDao.saveUserMeal(userMeal);
    }

    @Override
    public List<UserMeal> getAllMeals() {

        return (List<UserMeal>) mealDao.getAllUserMeals();
    }

    @Override
    public boolean deleteMeal(int id) {
        return mealDao.deleteUserMeal(id);
    }

    @Override
    public UserMeal getMeal(int id) {
        return mealDao.getUserMeal(id);
    }

    @Override
    public void updateMeal(UserMeal userMeal) {
        mealDao.updateUserMeal(userMeal);
    }


    @Override
    public Collection<UserMeal> listAllUserMeals(int userid) {

        return mealDao.listByUser(userid);
    }


    // это нужно как - то обобщить, сделать с женериками или getbyfield ...
    @Override
    public Collection<UserMeal> getByDate(Date date) {
       // mealDao.getByField("date");
        return null;
    }

    @Override
    public Collection<UserMeal> getByDateRange(Date start, Date end) {
        // mealDao.getByField("date");
        return null;
    }

    @Override
    public Collection<UserMeal> getByCalories(int calories) {
        // mealDao.getByField("ccal");
        return null;
    }

    @Override
    public Collection<UserMeal> getByCaloriesRange(int min, int max) {
        return null;
    }




}
