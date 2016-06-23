package ru.headrich.topjava.DAO;

import junit.framework.TestCase;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;

import java.sql.Date;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Montana on 16.06.2016.
 */

//C ТЕСТАМИ ВЕСЕЛЕЕ ПРОГАТЬ ТАК КАК СРАЗУ ПРОВЕРИЛ ЧЕ КАК, А НЕ НА АБУМ)))) ТЕРЬ БУДУ ТЕСТЫ ПИСАТЬ ЧАЩЕ
public class UserDAOImplTest extends TestCase {

    static UserDAOImpl userDAO = new UserDAOImpl();
    static UserMealDAOImpl userMealDAO = new UserMealDAOImpl();

    static{
        Properties cp = new Properties();
        cp.setProperty("url","jdbc:mysql://localhost:3306/calories?allowMultiQueries=true");
        cp.setProperty("username","root");
        cp.setProperty("password","root");
        cp.setProperty("driver","com.mysql.jdbc.Driver");
        userDAO.setCm(ConnectionManager.getInstance(cp));
        userMealDAO.setCm(ConnectionManager.getInstance(cp));
    }

    public void testGetUser() throws Exception {
        assertNull(userDAO.getUser(2));
        assertEquals(1,userDAO.getUser(1).getId().intValue());
        //assertEquals("Iban@gmail.com",userDAO.getUser(1).getEmail());
        User uuu = userDAO.getUser(9);
        System.out.println(uuu + " have meals : " + uuu.getMeals());
    }

    public void testDeleteUser() throws Exception {
        assertTrue(userDAO.deleteUser(24));
        //благодаря внешнему ключу с каскдным удалением, удаляются и usermeals тоже.Ура!
        //однако без него , в случае галимого разраба базы, тут нужно руками, иил дописывать код улаения
    }

    public void testSaveUser() throws Exception {
        User u = new User("TESTROLE@@@2BATCHING","tutututu","tratata", Role.ROLE_USER);
        ArrayList<UserMeal> userMeals = new ArrayList<>(3);
        UserMeal um1 = new UserMeal(new Date(Clock.systemUTC().millis()),"desc111",100);
        um1.setUser(u);
        userMeals.add(um1);
        um1=new UserMeal(new Date(Clock.systemUTC().millis()),"desc222",101);
        um1.setUser(u);
        userMeals.add(um1);
        um1=new UserMeal(new Date(Clock.systemUTC().millis()),"desc333",102);
        um1.setUser(u);
        userMeals.add(um1);
        u.setMeals(userMeals);
        User uuu = userDAO.saveUser(u);
        ArrayList<UserMeal> umls = (ArrayList<UserMeal>) userMealDAO.listByUser(uuu.getId());
        System.out.println(uuu);
        System.out.println(umls);


        //assertNull(userDAO.saveUser(u));

        //а сохранет ли он продукты юзера - неа/ надо вручную


    //почитать еще раз про автоинкремент поля, чтобы было 1,2,3, а не 1,7,9 т.е отталкиваться от предидущего значения, а не счетчика
    }

    public void testUpdateUser() throws Exception {
        /*User u = userDAO.getUser(1);
        u.setName("Updated2Ivan2");
        userDAO.updateUser(u);
        assertEquals("Updated2Ivan2",userDAO.getUser(1).getName());*/
        //fail(); System.exit(1);
        User u = userDAO.getUser(20);
       /* UserMeal num = new UserMeal(new Date(Clock.systemUTC().millis()),"fdgdfgdgdg",55555);
        num.setUser(u);
        List<UserMeal> uml = u.getMeals().subList(0,3);
        uml.get(0).setDescription("EDITED!!!!");
        System.out.println(uml);
        uml.add(num);
        u.setMeals(uml);*/
        u.addAuthorities(Role.ROLE_ADMIN);
        userDAO.updateUser(u);
//сделать так чтобы при обновлении списка милсов, удалялись те, которых нет вн новом списке
        System.out.println(userDAO.getUser(20));
        //System.out.println(userMealDAO.listByUser(20));




    }

    public void testGetAllUsers() throws Exception {
        ArrayList<User> ul = (ArrayList<User>)userDAO.listUsers();
        System.out.println(ul.toString());
        assertEquals(2,ul.size());

    }

}