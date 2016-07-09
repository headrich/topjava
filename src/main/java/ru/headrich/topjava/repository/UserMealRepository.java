package ru.headrich.topjava.repository;

import ru.headrich.topjava.model.UserMeal;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Montana on 07.06.2016.
 */
public interface UserMealRepository {
    UserMeal addMeal(UserMeal userMeal,int iduser);

    List<UserMeal> getAllMeals();

    List<UserMeal> getAllUserMeals(int iduser);

    boolean deleteMeal(int id);

    //if composite priamry key( int id,user id) но нужно ли это? //
    //если это будет сервис где будут еще просто продукты, или они будут от админа? вообщем-то можно и так
    // но может и быть так типа калоризатора, чтоб добавить продуктов, но не удалять  при удалении пользователя например.!!
    //ведь это просто продукты, а не те что схавал человек.
    //вполне можно сделат модерацию,а продукты-просто продукты приписывать главному админу.
    // UserMeal getMeal(int id,int iduser);
    UserMeal getMeal(int id);

    boolean updateMeal(UserMeal userMeal);

    Collection<UserMeal> getByDate(Date date, int iduser);

    Collection<UserMeal> getByDateRange(Date start, Date end, int iduser);

    Collection<UserMeal> getByCaloriesRange(int min,int max,int iduser);



}
