package ru.headrich.topjava.DAO;

import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static ru.headrich.topjava.DAO.RDB.CommonRDBDAO.connection;

/**
 * Created by Montana on 07.06.2016.
 */

//сделать нормально
    //сделать тесты
    //данные в запросах должны быть извне( имя таблиц и тд и тп)
public class UserMealDAOImpl implements UserMealDAO{
  //  @Autowired  попытаться сделать аннотацию и внедрение бина
    static ConnectionManager cm;
    static{
        Properties cp = new Properties();
        cp.setProperty("url","jdbc:mysql://localhost:3306/calories");
        cp.setProperty("username","root");
        cp.setProperty("password","root");
        cp.setProperty("driver","com.mysql.jdbc.Driver");
        cm = ConnectionManager.getInstance(cp);

    }

    public ConnectionManager getCm() {
        return cm;
    }

    public UserMealDAOImpl setCm(ConnectionManager cm) {
        this.cm = cm;
        return this;
    }

    @Override
    public UserMeal getUserMeal(int id) {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Connection connection = null;
        UserMeal um = null;
        try {
            connection = cm.getConnection();
            ps = connection.prepareStatement("SELECT * FROM meal WHERE idmeal =?");
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if(!rs.isBeforeFirst()){
                System.out.println("not found meal with id:"+id);
            }else {
                while (rs.next()) {
                    um = new UserMeal();
                    um.setId(rs.getInt(1));
                    um.setDescription(rs.getString(2));
                    um.setCaloiries(rs.getInt(3));
                    um.setDate(rs.getDate(4));
                    um.setUser(SimpleDAOFactory.getInstance().getUserDAO().getUser(rs.getInt(5)));

                }
            }
            rs.close();
            ps.close();
        }catch(SQLException e){
            System.err.print("SQL ERRROR Reading meal failed" + e.getMessage());
            //e.printStackTrace();
        } finally{
                cm.disconnect();

        }

        return um;
    }

    @Override
    public boolean deleteUserMeal(int id) {
        Connection connection=null;
        PreparedStatement ps=null;
        boolean isdeleted = false;
        try {
            connection = cm.getConnection();
            ps = connection.prepareStatement("DELETE FROM meal WHERE idmeal =?");
            ps.setInt(1,id);
            int mc  = ps.executeUpdate();
            if(mc!=-1 && mc!=0){
                isdeleted=true;
                System.out.println("Delete meal: count modified : "+mc);
            }else{
                System.out.print("Delete meal: failed :count modified :"+mc);
            }
            ps.close();

        }catch(SQLException e){
            System.err.print("SQL ERRROR Delete meal failed" + e.getMessage());
            //e.printStackTrace();
        }  finally{
                cm.disconnect();
        }

        return isdeleted;
    }

//вместо повторных запросов аля ласт инсерт ид, переделать в один препаред запрос с addbatch
    @Override
    public UserMeal saveUserMeal(UserMeal userMeal) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = cm.getConnection();
            /*connection.prepareStatement("" +
                    "INSERT INTO USER VALUES (DEFAULT,"+
                    Arrays.asList(new Object[]{userMeal.getCaloiries(),userMeal.getDescription(),userMeal.getDate()}).stream().map(v -> v.toString()).collect(Collectors.joining(", ")) )
                    .executeUpdate();
            */
            ps = connection.prepareStatement("INSERT INTO meal VALUES (DEFAULT , ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS);
            ps.setInt(2,userMeal.getCaloiries());
            ps.setString(1,userMeal.getDescription());
            ps.setDate(3,userMeal.getDate());
            ps.setInt(4,userMeal.getUser().getId());
            int mc = ps.executeUpdate();
            if(mc!=-1 && mc!=0){
                rs = ps.getGeneratedKeys();
                while(rs.next()) userMeal.setId(rs.getInt(1));
                System.out.println("Save UserMeal: Count modified: "+mc+" ::  idmeal = "+userMeal.getId());

                //rs = connection.prepareStatement(" SELECT  idmeal FROM  meal  ORDER by idmeal DESC LIMIT 1").executeQuery();


            }else{
                System.out.println("Save meal: failed");
            }
            ps.close();

        }catch(SQLException e){
            System.out.println("SQL ERROR Save meal failed " + e.getMessage());
            //e.printStackTrace();
        } finally{

                cm.disconnect();

        }

        return userMeal;
    }

    @Override
    public void updateUserMeal(UserMeal userMeal) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = cm.getConnection();
            ps= connection.prepareStatement("UPDATE meal SET ccal=?,descr=?,date=?,user=? WHERE idmeal=?");
            ps.setInt(1,userMeal.getCaloiries());
            ps.setString(2,userMeal.getDescription());
            ps.setDate(3,userMeal.getDate());
            ps.setInt(4,userMeal.getUser().getId());
            ps.setInt(5,userMeal.getId());
            int mc = ps.executeUpdate();
            if(mc!=-1 && mc !=0){
                System.out.println("Update meal: count modified: "+mc);
            }else{
                System.out.println("Update meal: failed");
            }
            ps.close();

        }catch(SQLException e){
            System.err.print(" SQL ERRROR Update meal failed" + e.getMessage());
            //e.printStackTrace();
        }finally{

                cm.disconnect();

        }

    }

    public void SaveOrUpdateMeal(UserMeal meal){
        //если у нас данные из базы
        //еще вариант есть в запросе записать  ON DUPLICATE KEY UPDATE col1=val1,col2=val2
        //это если у нас с одинаковым ключом они (или с уникальным)

        if(meal.getId()==null){
            saveUserMeal(meal);
        }else{
            updateUserMeal(meal);
        }
    }


//минуточку. Это же милсы, там вполне себе могут быть дубликаты. Особенно если мы просто что-то добавляем.
    // тогда такой момент. как мы можем поменять то что еще не добавленно? никак.
    // потому мы добавляем все подряд, и меняем лишь то что подлежит изменению явно.

    @Override
    public List<UserMeal> getAllUserMeals() {
        Connection connection =null;
        ResultSet rs = null;
        List<UserMeal> userMeals = null;
        try{
            connection = cm.getConnection();
            rs = connection.prepareStatement("SELECT * FROM meal").executeQuery();
            userMeals = new ArrayList<>();
            while (rs.next()){
                UserMeal um =new UserMeal(rs.getDate("date"),rs.getString("descr"),rs.getInt("ccal"));
                um.setId(rs.getInt(1));
                userMeals.add(um);
            }
        }catch(SQLException e) {
            System.err.print("SQL ERRROR Read meals failed" + e.getMessage());
            //e.printStackTrace();
        }finally {
            cm.disconnect();
        }

        return userMeals;
    }

    public List<UserMeal> listByUser(int iduser){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<UserMeal> userMeals = null;
        try{
            connection = cm.getConnection();
            ps = connection.prepareStatement("SELECT * FROM meal LEFT JOIN user ON meal.user = user.iduser WHERE meal.user=? order by idmeal");
            ps.setInt(1,iduser);
            rs = ps.executeQuery();
            if(!rs.isBeforeFirst()){
                System.out.println("Not found meals by user with id :"+ iduser);
            }else{
                userMeals = new ArrayList<>();
                User u = new User();
                u.setMeals(userMeals);
                int rc = 0;//для того тчобы записать только один раз юзера. он в каждой строчке одинаковый
                while(rs.next()){
                    UserMeal um = new UserMeal();
                    um.setId(rs.getInt(1));
                    um.setDescription(rs.getString(2));
                    um.setCaloiries(rs.getInt(3));
                    um.setDate(rs.getDate(4));
                    userMeals.add(um);
                    if(rc==0) {
                        u.setId(rs.getInt(5));
                        u.setName(rs.getString(7));
                        u.setEmail(rs.getString(8));
                        u.setPassword(rs.getString(9));
                        u.setEnabled(rs.getBoolean(10));
                        u.setRegistered(rs.getDate(11));
                    }
                    rc++;

                }
                //тут мог бы быть второй запрос на подтягивание юзера из базы, но его нет
                //потому что лучше одним запросом,с объединением таблиц.
                //а еще можно воспользоваться userdao.getuserbyid(), который подтягивает список meals
                //если подумать что было бы если не по id выбирать, а по имени или мылу, то толком то ниче не изменится
                // а значит весь этот повторяемый код можно обобщить получением по произвольному полю

            }
            rs.close();
            ps.close();
        }catch (SQLException e){
            System.out.println("SQL ERRROR Read meals error: "+ e.getMessage());
        }finally {

                cm.disconnect();

        }

        return userMeals;
    }


    @Override
    public UserMeal getByField(String field) {
        return null;
    }

    @Override
    public List<UserMeal> listByField(String field) {
        return null;
    }

}
