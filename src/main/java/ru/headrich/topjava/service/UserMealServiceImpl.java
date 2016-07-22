package ru.headrich.topjava.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.repository.UserMealRepository;
import ru.headrich.topjava.util.exception.NotFoundException;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Name;

/**
 * Created by Montana on 07.06.2016.
 */
@Service
public class UserMealServiceImpl implements UserMealService {
    public UserMealServiceImpl() {
    }

    @Inject
    private UserMealRepository userMealRepository;

    public void setUserMealRepository(UserMealRepository userMealRepository) {
        this.userMealRepository = userMealRepository;
    }

    public UserMealServiceImpl(UserMealRepository userMealRepository) {
        this.userMealRepository = userMealRepository;
    }

    @Override
    public UserMeal addMeal(UserMeal userMeal) {
        return null;
    }

    @Override
    public List<UserMeal> getAllMeals() {
        return userMealRepository.getAllMeals();
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
