package ru.headrich.topjava.DAO;

import com.mysql.jdbc.Field;
import com.mysql.jdbc.Statement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import ru.headrich.topjava.DAO.ORMengine.proxy.SimpleCloner;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;
import ru.headrich.topjava.util.ConfigLoader;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Created by Montana on 07.06.2016.
 */

//сделать хэширование паролей
    //сделать подтягивание из связных таблиц РОЛИ + (удаление там каскадное)
    //сделать нормально и качественно
    //написать тесты с Junit!!
public  class UserDAOImpl implements UserDAO {
    StateHolder session ;
    static ConnectionManager cm;
    static{
       /* Properties cp = new Properties();
        cp.setProperty("url","jdbc:mysql://localhost:3306/calories?allowMultiQueries=true");
        cp.setProperty("username","root");
        cp.setProperty("password","root");
        cp.setProperty("driver","com.mysql.jdbc.Driver");*/
        Properties cp = null;
        try {
            cp = new Properties(ConfigLoader.loadProps());
        } catch (IOException e) {
            System.out.println("Load config error;" + e.getMessage());
        }
        cm = ConnectionManager.getInstance(cp);

    }

    private static final String GET_USER_LAZY_QUERY = "SELECT * FROM USER JOIN meal where user.iduser = ?";
    private static final String GET_USER_AND_MEALS_QUERY = "SELECT * FROM USER JOIN meal ON user.iduser = meal.user WHERE iduser =? order by iduser;";
    private static final String GET_USER_ROLES_QUERY = "SELECT role FROM  calories.authority JOIN calories.role ON authority.authority=role.idrole WHERE authority.user = ?";
    private static final String GET_ALLUSERS_AND_MEALS_QUERY = "SELECT * FROM calories.user LEFT JOIN calories.meal ON user.iduser = meal.user order by iduser";
    private static final String GET_ALLUSERS_LAZY_QUERY = "SELECT * FROM calories.user";

    public UserDAOImpl() {
        session = new StateHolder();
    }

//TODO тут и будет у нас храниться инфа о состоянии текущего соединения, сущностей, и т.д.
    // ага и походу сразу двух зайцев. Если мы тут все храним, то все используеммы не тут, а в другом слое - repository


//DAO синглтон по умолчанию. оно как сессионфактори. А сессия она уже именно как стэйтхолдер.
    /*

    There is no need to make your DAOs session-scoped.

Though Hibernate session has a state, under properly configured transaction management its state is bound to transactions (i.e. different DAO methods called inside the same transactions share the same session, whereas the same method called from different transactions use different sessions).

So, your DAOs are effectively stateless, and should be singleton-scoped (i.e. default scope).
     */
    class StateHolder<T>{

        private T obj;
        private User persistUser;

        private User currentUser;
        private Map<String,? super Object> fieldsToUpdate;

        public User getPersistUser() {
            return persistUser;
        }

        public void setPersistUser(User persistUser) {
            this.persistUser = persistUser;
        }

        public User getCurrentUser() {
            return currentUser;
        }

        public void setCurrentUser(User currentUser) {
            this.currentUser = currentUser;
        }

        public <T> void compareStates(T persist, T current){
            //reflection getting allfields
            if(persist!=null && current!=null)
                 new EqualsBuilder().append(persist,current).isEquals();


        }

        //пока сделаю конкретику. потом мб обоббщу это тупой вариант. не хочу его делать
        //лучше динамически отслеживать изменения.чем сравнивать так
        // при том че это, постоянно сравнивать чтоли пред апдейтом, а если нам нужн обновить кучу юзеров сразу. у всех такая полнопроверка. не не катит.
        public void compareUsers(User persist, User current){
            //пройти по всем полям рефлеторно, сравнить значения.
            if (!persist.getAuthorities().equals(current.getAuthorities())) {
                //имена полей лучше получать из аннотаций методов, тогда можно легко генерировать запросы.
                fieldsToUpdate.put("authorities",current.getAuthorities());
            }
            if(!persist.getId().equals(current.getId())){
                fieldsToUpdate.put("iduser",current.getId());
            }
            if(!persist.getName().equals(current.getId())){
                fieldsToUpdate.put("name",current.getName());
            }
            if(!persist.getEmail().equals(current.getId())){
                fieldsToUpdate.put("email",current.getEmail());
            }
            if(!persist.getPassword().equals(current.getId())){
                fieldsToUpdate.put("password",current.getPassword());
            }
            if(!persist.getRegistered().equals(current.getId())){
                fieldsToUpdate.put("registered",current.getRegistered());
            }
            if(!persist.getMeals().equals(current.getMeals())){
                fieldsToUpdate.put("meals",current.getMeals());
            }

        }



    }






    public ConnectionManager getCm() {
        return cm;
    }

    public UserDAOImpl setCm(ConnectionManager cm) {
        this.cm = cm;
        return this; // тут типа паттерна декоратор или фасад что то такое, когда получаешь улучшенный объект и можно делать так  obj.сделайтсобъектом1.сделатьсобъектом2....
    }
//INFO  работаем не с connection, а со Statement .
    //это как session.begintransaction в хибернейте

    //для получения инфы о бд  есть класс DataBaseMetaData

    //preparestatement = подготовленный запрос, оно быстрее
    //и сам запрос составить проще, where id= ?  параметры можно добавлять командой ps.setXXX
    // execute - редко executeUpdate - для (CUD). Возвращает число модифицированных записей
    //executeQuery для получения ResultSet набора данных( оператор SELECT)

    //можно будет поробовать придумать ленивую загрузку. Подтягивать или не подтягивать сущности по внешнему ключу.
    @Override
    public User getUser(int id)  {
        PreparedStatement st=null;
        //ResultSet rs=null;
        //Connection connection = null;
        User u = null;
        //PreparedStatement getUser = null;
        //PreparedStatement getUserRoles =null;

        Savepoint uam;
        Savepoint rol;
        boolean lazy =false;

        try(Connection connection = cm.getConnection()) {


            //это java 7 try with resource block , который сам закрывает потоки ресурсов!!

            //connection = cm.getConnection();

            //такой запрос конечно подойдет. но он не оптимизирован. из-за дубликатов


            /*
            9	pine	apple@gmail.com	fghjk	1	2016-06-17	1	хлебушек для энергии	26	2016-06-17 00:00:00.000000	9	1	ROLE_ADMIN	1	9
            9	pine	apple@gmail.com	fghjk	1	2016-06-17	1	хлебушек для энергии	26	2016-06-17 00:00:00.000000	9	3	ROLE_GUEST	3	9
            9	pine    apple@gmail.com	fghjk	1	2016-06-17	10	ghj	                    34	2016-06-17 00:00:00.000000	9	1	ROLE_ADMIN	1	9
            9	pine	apple@gmail.com	fghjk	1	2016-06-17	10	ghj	                    34	2016-06-17 00:00:00.000000	9	3	ROLE_GUEST	3	9
             */
            //а потому лучше сделать несколько запросов батчами , один для
            /*st = connection.prepareStatement("SELECT * FROM USER LEFT JOIN meal ON user.iduser = meal.user " +
                    "LEFT JOIN (SELECT * FROM calories.role, calories.authority  WHERE role.idrole=authority.authority) as R ON user.iduser = R.user WHERE iduser =" + id+ " order by iduser");*/



            //лучше батчи!! ОДНАКО!! БАТЧИ ТОЛЬКО ДЛЯ ЛЕГКИХ ЗАПРОСОВ ОБНОВЛЕНИЯ/ДОБАВЛЕНИЯ. РЕЗУЛЬТАТ С НИМИ НЕ ОБРАБОТАТЬ НЕ ПОЛУЧИТЬ.
           /*
            //ЧТОБЫ СРАБОТАЛо несколько запросов на несколько результатов нужно добавить параметр к урл "jdbc:mysql://localhost:3306/calories?allowMultiQueries=true"
            st = connection.prepareStatement("SELECT * FROM USER JOIN meal ON user.iduser = meal.user WHERE iduser =? order by iduser;\n " +
                    "SELECT role FROM  calories.authority JOIN calories.role ON authority.authority=role.idrole WHERE authority.user = ?");
                     st.setInt(1,id);
            st.setInt(2,id);
            if(!st.execute()){
                System.out.println("Get user fail");
            }else{
                rs = st.getResultSet();
                if(!rs.isBeforeFirst()){
                    System.out.println("not found user with id:"+id);

                }else {
                    ArrayList<UserMeal> ml = new ArrayList<>();
                    int rc =0;
                    while (rs.next()) {
                        if(rc==0) {
                            u = new User();
                            u.setMeals(ml);
                            u.setId(rs.getInt(1));
                            u.setName(rs.getString(2));
                            u.setEmail(rs.getString(3));
                            u.setPassword(rs.getString(4));
                            u.setEnabled(rs.getBoolean(5));
                            u.setRegistered(rs.getDate(6));
                        }
                        //parsing usermeal
                        UserMeal um = new UserMeal();
                        um.setId(rs.getInt(7));
                        um.setDescription(rs.getString(8));
                        um.setCaloiries(rs.getInt(9));
                        um.setDate(rs.getDate(10));
                        um.setUser(u);
                        ml.add(um);

                    }

                }
                if(u!=null) //тут нужно что-то поумнее. связанное с результатом запроса
                if(st.getMoreResults()){
                    rs = st.getResultSet();
                    if(!rs.isBeforeFirst()){
                        System.out.println("not found user role:"+id);

                    }else {
                       Set<Role> roles = new HashSet<>();
                        u.setAuthorities(roles);
                        while(rs.next()){
                            roles.add(Role.valueOf(rs.getString(1)));
                        }

                    }
                }
                rs.close();

            }
            st.close();
            */

            //хороший запрос. получаем один столц с нужными данными
            //st.addBatch("Select role From  calories.authority join calories.role on authority.authority=role.idrole where authority.user = ?");
            //getUserRoles = connection.prepareStatement("Select role From  calories.authority join calories.role on authority.authority=role.idrole where authority.user = ?");
            //getUserRoles.setInt(1,id);

            //rs = st.executeQuery();

           /* if(!rs.isBeforeFirst()){
                System.out.println("not found user with id:"+id);

            }else {
                ArrayList<UserMeal> ml = new ArrayList<>();
                int rc =0;
                while (rs.next()) {
                    if(rc==0) {
                        u = new User();
                        u.setMeals(ml);
                        u.setId(rs.getInt(1));
                        u.setName(rs.getString(2));
                        u.setEmail(rs.getString(3));
                        u.setPassword(rs.getString(4));
                        u.setEnabled(rs.getBoolean(5));
                        u.setRegistered(rs.getDate(6));
                    }
                    //parsing usermeal
                    UserMeal um = new UserMeal();
                    um.setId(rs.getInt(7));
                    um.setDescription(rs.getString(8));
                    um.setCaloiries(rs.getInt(9));
                    um.setDate(rs.getDate(10));
                    um.setUser(u);
                    ml.add(um);

                }



                rs.close();
                st.close();




            }*/


            String userquery;
            if(lazy){
                userquery = GET_USER_LAZY_QUERY;
                //тогда получается нужно будет подгружать эти meals по востребованности, т.е отслеживать (каким образом?) вызов getmeals из user/ и подгружать..
                //притом нужен другой метод getmealbyuser(user) чтобы не подгружать user потворно. а просто его установить
                //но это подождет. По карйней мере, я понимаю как это можно сделать, а делать это будет ЖПА - хибернейт, с которым тоже предстоит разобраться
            }else {
                userquery = GET_USER_AND_MEALS_QUERY;
            }

            try(PreparedStatement getUser = connection.prepareStatement(userquery)){
                getUser.setInt(1,id);
                try(ResultSet rs = getUser.executeQuery()){
                    if (!rs.isBeforeFirst()) {
                        System.out.println("NOT FOUND USER with id : " + id);
                        rs.close();
                    } else {
                        ArrayList<UserMeal> ml = new ArrayList<>();

                        int rc = 0;
                        while (rs.next()) {
                            if (rc == 0) {
                                u = parseUserResultSet(rs);
                                u.setMeals(ml);
                            }

                            if(!lazy) {
                                //parsing usermeal
                                UserMeal um = parseUserMealResultSet(rs);
                                um.setUser(u);
                                ml.add(um);
                            }

                            rc++;
                        }

                    }
                }

            }catch(Exception e){
                System.out.println("Fetching user error" + e.getMessage());
            }
            if(u!=null)
            try(PreparedStatement getUserRoles = connection.prepareStatement(GET_USER_ROLES_QUERY)){
                getUserRoles.setInt(1,id);

                try(ResultSet rs = getUserRoles.executeQuery()) {
                    if (!rs.isBeforeFirst()) {
                        System.out.println("Not found users roles");
                        rs.close();
                    } else {
                        Set<Role> roles = new HashSet<>();
                        u.setAuthorities(roles);
                        while (rs.next()) {
                            roles.add(Role.valueOf(rs.getString(1)));
                        }
                        rs.close();
                    }
                }
            }catch(Exception e){
                System.out.print("Fetching user roles Error" + e.getMessage());
            }


        }
        catch(SQLException e){
            System.out.println(" SQL ERROR Read user error:  " + e.getMessage());
        }

        session.persistUser = u;

        try {
            session.currentUser = SimpleCloner.generateClone(u);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return session.currentUser;
    }

    private User parseUserResultSet(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt(1));
        u.setName(rs.getString(2));
        u.setEmail(rs.getString(3));
        u.setPassword(rs.getString(4));
        u.setEnabled(rs.getBoolean(5));
        u.setRegistered(rs.getDate(6));
        return u;
    }
    private UserMeal parseUserMealResultSet(ResultSet rs) throws SQLException{
        UserMeal um = new UserMeal();
        um.setId(rs.getInt(7));
        um.setDescription(rs.getString(8));
        um.setCaloiries(rs.getInt(9));
        um.setDate(rs.getDate(10));
        return um;
    }
    //в 2 запроса
    public User getUser2(int id)  {
        PreparedStatement st=null;
        ResultSet rs=null;
        Connection connection = null;
        User u = null;
        try {
            connection = cm.getConnection();

             st = connection.prepareStatement("SELECT * FROM USER WHERE iduser =" + id);
             rs = st.executeQuery();

            if(!rs.isBeforeFirst()){
                System.out.println("Not found user with id:"+id);

            }else {

                while (rs.next()) {
                    u = new User();
                    u.setId(rs.getInt(1));
                    u.setName(rs.getString(2));
                    u.setEmail(rs.getString(3));
                    u.setPassword(rs.getString(4));
                    u.setEnabled(rs.getBoolean(5));
                    u.setRegistered(rs.getDate(6));
                }


                rs.close();
                //fetch meals!!! сделать грамотно, чтобы подтягивалось и при геталл
                //или с батчем
                //проще сделать запрос с JOIN !!!!!!!!!!LEFTJOIN SELECT * FROM calories.user LEFT JOIN calories.meal ON user.iduser = meal.user order by iduser
       /*       1	Updated2Ivan2	Iban@gmail.com	parolb	1	2016-06-17	7	хлебушек для энергии	26	2016-06-17 00:00:00.000000	1
                1	Updated2Ivan2	Iban@gmail.com	parolb	1	2016-06-17	4	хлебушек для энергии	26	2016-06-17 00:00:00.000000	1
                1	Updated2Ivan2	Iban@gmail.com	parolb	1	2016-06-17	3	хлебушек для энергии	26	2016-06-17 00:00:00.000000	1
                1	Updated2Ivan2	Iban@gmail.com	parolb	1	2016-06-17	8	хлебушек для энергии	26	2016-06-17 00:00:00.000000	1
                9	pine	apple@gmail.com	fghjk	1	2016-06-17	1	хлебушек для энергии	26	2016-06-17 00:00:00.000000	9
                9	pine	apple@gmail.com	fghjk	1	2016-06-17	10	ghj	34	2016-06-17 00:00:00.000000	9*/
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM meal WHERE user = ?");
                ps.setInt(1, u.getId());
                rs = ps.executeQuery();
                ArrayList<UserMeal> ml = new ArrayList<>();
                while (rs.next()) {
                    UserMeal um = new UserMeal();
                    um.setId(rs.getInt(1));
                    um.setDescription(rs.getString(2));
                    um.setCaloiries(rs.getInt(3));
                    um.setDate(rs.getDate(4));
                    um.setUser(u);

                    ml.add(um);
                }
                u.setMeals(ml);

                rs.close();
                ps.close();
            }

            st.close();
        }catch(SQLException e){
            System.out.println(" SQL ERROR Read user error:  " + e.getMessage());
        }finally{

                    cm.disconnect();

        }

        return u;
    }

    //
    @Override
    public boolean deleteUser(int id) {
        Connection connection=null;
        PreparedStatement st=null;
        boolean isdeleted = false;
        try {
            connection = cm.getConnection();
            st = connection.prepareStatement("DELETE FROM USER WHERE iduser =" + id);
            int mc = st.executeUpdate();
            if(mc!=0 && mc!=-1){
                System.out.println("Deleting user: count modified :" + mc);
                isdeleted = true;
            }else{
                System.out.print("Delete user: failed :count modified :"+mc);
            }
            st.close();

        }catch(SQLException e){
            System.out.println(" SQL ERROR Delete user error:  " + e.getMessage());
        } finally{
                cm.disconnect();

        }

        return isdeleted;
    }


    @Override
    public User saveUser(User user) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Savepoint savepointUser = null;
        try{
            connection = cm.getConnection();

            /* Если Statement
            ps = connection.createStatement.executeUpdate("" +
                    "INSERT INTO USER VALUES (DEFAULT,"+
                    Arrays.toString(new Object[]{user.getName(), user.getEmail(), user.getPassword(), user.isEnabled(), user.getRegistered()}).replace("[","").replace("]","") );
            */
            //в случае PrepareStatement
            ps=connection.prepareStatement("INSERT INTO user VALUES (DEFAULT ,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,user.getName());
            ps.setString(2,user.getEmail());
            ps.setString(3,user.getPassword()); //java.util Base64 в помощь хэшированию
            ps.setBoolean(4,user.isEnabled());
            ps.setDate(5,user.getRegistered());


            int mc = ps.executeUpdate();

            if(mc!=-1 && mc!=0){
                rs = ps.getGeneratedKeys();
                while(rs.next()) user.setId(rs.getInt(1));
                System.out.println("Save User: Count modified:"+mc + " :: iduser= "+user.getId());
                //rs = connection.prepareStatement(" SELECT LAST_INSERT_ID()").executeQuery();
                rs.close();

            }else{
                System.out.println("Saving user fails");
            }

             savepointUser = connection.setSavepoint("user");

            //ps.close();
            //РОЛИ!! Есть таблица РОЛИ( в ней просто роли) и есть таблица authority -таблица соответвия userу роли.
            //самописый варик со стрингбилдером
           /* StringBuilder  rolquery= new StringBuilder("INSERT INTO authority VALUES ");
            for( Iterator<Role> ir = user.getAuthorities().iterator(); ir.hasNext();){
                Role r = ir.next();
                rolquery.append( "((SELECT idrole FROM role WHERE role.role='"+r.toString()+"' LIMIT 1),"+user.getId()+")" );
                if(ir.hasNext()) rolquery.append(", ");

            }
            mc = ps.executeUpdate(rolquery.toString());
            if(mc!=-1 && mc!=0) {
                System.out.println("Save User: ADD ROLE :Count modified:" + mc);
            }else{
                System.out.println("Save User: ADD ROLE :failed! Count modified:" + mc);
            }*/
            //варик с preparestatement#addbatch https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html#batch_updates
            ps=connection.prepareStatement("INSERT INTO authority VALUES ((SELECT idrole FROM role WHERE role.role=? LIMIT 1),?)");
            for(Role role: user.getAuthorities()){
                ps.setString(1,role.toString());
                ps.setInt(2,user.getId());
                ps.addBatch();
            }

            int[] amc  = ps.executeBatch();
            for(int a: amc){
                if(a!=-1 && a!=0) {
                    System.out.println("Save User: ADD ROLE :Count modified:" + a);
                }else{
                    System.out.println("Save User: ADD ROLE :failed! Count modified:" + a);
                }
            }



            ps.close();


            //USERMEALS элегантное решение. за исключение того что в одном дао  другое. такого быть не должно.вроде как
            user.getMeals().parallelStream().forEach(SimpleDAOFactory.getInstance().getUserMealDAO()::SaveOrUpdateMeal);




        }catch(SQLException e){
            //e.printStackTrace();
            System.out.println("SQL ERRROR Saving user fails:  " + e.getMessage());
            //connection.rollback(savepointUser);
            //тут нужно делать ролбеки! в приложении то он нулл будет, а вот в базе записался гад!
            //в этом польза общего блока трай, тк как п о смыслу выполняется одна задача.
            user = null;
        }finally{

                cm.disconnect();

        }
        session.persistUser = user;
        try {
            session.currentUser = SimpleCloner.generateClone(user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return session.currentUser;
    }

    // путанница между теми что из базы и теми что тут.

    //можно удалить его и заного добавить.
    @Override
    public void updateUser(User user) throws SQLException {

        Connection connection = null;
        //PreparedStatement updateUser = null;
        //PreparedStatement updateUserRole = null;

        try {
            connection = cm.getConnection();
            connection.setAutoCommit(false);
            try(PreparedStatement updateUser = connection.prepareStatement("UPDATE user set name=?, email=?, password=?, enabled=?, registered=? WHERE iduser=?");) {
                updateUser.setString(1, user.getName());
                updateUser.setString(2, user.getEmail());
                updateUser.setString(3, user.getPassword());
                updateUser.setBoolean(4, user.isEnabled());
                updateUser.setDate(5, user.getRegistered());
                updateUser.setInt(6, user.getId());

                int mc = updateUser.executeUpdate();

                if (mc != -1 && mc != 0) {
                    System.out.println("Update user: count modified: " + mc);
                } else {
                    System.out.println("Update user: failed");
                }
            }

            //а вот роли можно менять и апдейтить . такое вполне возможно) поэтому:
            //узнать как обновить только измененные поля!!!!!!!!!!!!!!
//тут нужно или добавить или изменить. непрально сделал
            //проблема в том, что если у одного юзера несколько ролей, то обновляет он обе записи. может нужно стертеть - азписать?
            //в рамках этой задачи можно удалить и записать заного, т.к ролей не так уж и много. однако в общем случае это не годится.

            // можно применить подход как в хибернейт. Создать для загруженного из бд прокси объект-копию, и сравнивать с измененным клиентом объектом.
            //значит нужно реализовать функционал отслеживания изменений, который знает что изменилось и соответввенно какое поле нужно обновить.
            // плюс ко всему можно отслеживать вызовы методов получения  дочерних коллекций для ленивой загрузки.
            //очень интересная задача. хорошо что я с ней определился.
            //ковальски - варианты!!
            //нужно save or update if exist.
            // репласе скл команда, которая заменяет текущую запись с таким же примари на новую. вот когда у меня подабавлялось милсов , она бы помогла.
            //получаем  сет ролей. сравниваем с измененным.если такая есть - оставляем( не сохраняем), если такой нет - добавляем. Если убрали роль, тогда удаляем.
            //тогда  у нас будут батчи с запросом а не с прамаетрами



            try(PreparedStatement updateUserRole = connection.prepareStatement("UPDATE authority set authority=(SELECT idrole FROM role WHERE role=?) WHERE user=? ");) {
                for (Role r : user.getAuthorities()) {
                    updateUserRole.setString(1, r.toString());
                    updateUserRole.setInt(2, user.getId());
                    updateUserRole.addBatch();
                }

                int[] ra = updateUserRole.executeBatch();
                updateUserRole.clearBatch();
                for (int i=0;i<ra.length;i++) {
                    if (ra[i] == -2)
                        System.out.println("Execution " + i +
                                ": unknown number of rows updated");
                    else
                        System.out.println("Execution " + i +
                                "successful: " + ra[i] + " rows updated");
                }

            }
            connection.commit();

            //вот тут надо проверить какие милсы остануться, а какие удлаить.
            //путаницы нет, так как это апдейт.тут у юзера есть ид.
            //а нахер это вообще надо.а если огромные списки, и так постоянно делать когда он только имя поменять хотел...
            // UserMealDAOImpl umdao =  new UserMealDAOImpl().setCm(cm);
            //umdao.listByUser(user.getId());
            //user.getMeals().parallelStream().forEach(umdao::SaveOrUpdateMeal);

        } catch (SQLException e) {
            System.out.println(" SQL ERROR Update user: failed" + e.getMessage());
            if (connection != null) {
                connection.rollback();
            }
            //e.printStackTrace();
        }  finally {

                cm.disconnect();

        }



    }

    public void SaveOrUpdateMeal(User user) throws SQLException {
        if(user.getId()==null){
            saveUser(user);
        }else{
            updateUser(user);
        }
    }

    @Override
    public List<User> listUsers() {
        //fetch meals!!! сделать грамотно, чтобы подтягивалось и при геталл
        //Connection connection =null;
        ResultSet rs = null;
        List<User> users = null;
        boolean lazy = false;
        try(Connection connection = cm.getConnection();){

            String usersquery;
            if(lazy){
                usersquery=GET_ALLUSERS_LAZY_QUERY;
            }else{
                usersquery=GET_ALLUSERS_AND_MEALS_QUERY;
            }
            try(java.sql.Statement st = connection.createStatement()){

                rs = st.executeQuery(usersquery);
                users=new ArrayList<>();
                while(rs.next()){
                    User u = parseUserResultSet(rs);
                    users.add(u);

                    if(!lazy) {
                        ArrayList<UserMeal> ml = new ArrayList<>();
                        u.setMeals(ml);
                        //parsing usermeal
                        UserMeal um =parseUserMealResultSet(rs);
                        um.setUser(u);
                        ml.add(um);
                    }


                }
            }


        }catch (SQLException e) {
            System.out.print(" SQL ERROR Reading users failed" + e.getMessage());
            //e.printStackTrace();
        }finally {
            cm.disconnect();
        }

        return users;
    }

    public User getUserByField(){


        //тут как-нибудь рефлекшином
        return null;
    }
}
