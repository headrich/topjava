package ru.headrich.topjava.DAO;

import junit.framework.TestCase;
import org.junit.Test;
import ru.headrich.topjava.model.UserMeal;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Montana on 17.06.2016.
 */
public class UserMealDAOImplTest extends TestCase {

    static UserMealDAOImpl userMealDAO = new UserMealDAOImpl();
    static UserDAOImpl userDAO = new UserDAOImpl();

    static{
        Properties cp = new Properties();
        cp.setProperty("url","jdbc:mysql://localhost:3306/calories");
        cp.setProperty("username","root");
        cp.setProperty("password","root");
        cp.setProperty("driver","com.mysql.jdbc.Driver");
        ConnectionManager cm =  ConnectionManagerFactory.getConnectionManager("DV");
        userMealDAO.setCm(cm);
        userDAO.setCm(cm);

    }


    @Test
    public void testGetUserMeal() throws Exception {

        assertNull(userMealDAO.getUserMeal(1));

    }
    @Test
    public void testDeleteUserMeal() throws Exception {
        assertTrue(userMealDAO.deleteUserMeal(2));

    }
    @Test
    public void testSaveUserMeal() throws Exception {
        UserMeal um = new UserMeal(new Date(System.currentTimeMillis()),"хлебушек для энергии",26);
        um.setUser(userDAO.listUsers().get(9));
        assertEquals(8,userMealDAO.saveUserMeal(um).getId().intValue());
        //com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`calories`.`meal`, CONSTRAINT `fk_meal_user` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE CASCADE ON UPDATE NO ACTION)

    }
    @Test
    public void testUpdateUserMeal() throws Exception {
        UserMeal um = userMealDAO.getUserMeal(9);
        um.setUser(userDAO.getUser(10));
        userMealDAO.updateUserMeal(um);
        assertEquals(10,userMealDAO.getUserMeal(9).getUser().getId().intValue());
    }
    @Test
    public void testGetAllUserMeals() throws Exception {
        ArrayList<UserMeal> ml = (ArrayList<UserMeal>)userMealDAO.getAllUserMeals();
        System.out.println(ml.toString());
        assertEquals(8,ml.size());
    }


    @Test
    public void testgetUserMealsByUserID() throws Exception{
        ArrayList<UserMeal> ml = (ArrayList<UserMeal>)userMealDAO.listByUser(9);
        System.out.println(ml.toString());
        assertEquals(2,ml.size());
    }

    public static void main(String[] args) {


    }

}