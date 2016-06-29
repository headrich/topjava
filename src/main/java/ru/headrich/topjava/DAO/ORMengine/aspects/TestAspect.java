package ru.headrich.topjava.DAO.ORMengine.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.ORMengine.annotations.Table;
import ru.headrich.topjava.model.Role;
import ru.headrich.topjava.model.User;
import ru.headrich.topjava.model.UserMeal;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Montana on 23.06.2016.
 */
@Aspect
public class TestAspect {
    public static Map<List<String>,? super Object[]> updatedFieldsMap = new HashMap<>();

    int s;
    Logger LOG = LoggerFactory.getLogger(TestAspect.class);




   /* @Pointcut("execution(* ru.headrich.topjava.model.*.set*(..))" )
    public static void pc(){};*/



    //@Around("execution(void ru.headrich.topjava.model.*.set*(..))")
    public void catchChanghesAdvice(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("Around before" + jp.getArgs().toString());
        Object[] args = jp.getArgs();
        //args[0] = "Koreandr@gmail.lom";


        String methodname = jp.getSignature().getName();
        String target  = jp.getTarget().toString();
        System.out.println("target is "+target);
        String fieldname="";
        if(args!=null && args.length!=0){

            fieldname=methodname.substring(3).toLowerCase();

            Class targetClass= jp.getTarget().getClass();
            if(targetClass.isAnnotationPresent(Table.class)){
               String entityName = targetClass.getClass().getDeclaredAnnotation(Table.class).name();

                    ArrayList<Field> afl = new ArrayList<>();
                    Class c = targetClass;
                    //собираем все поля.
                    while(c!=null && !c.equals(Object.class)){

                            System.out.println(c.getName());
                            Collections.addAll(afl, c.getDeclaredFields());
                            c = c.getSuperclass();

                    }

                    String finalFieldname = fieldname;
                         Field targetSetField = afl.stream().filter(f->f.getName().equals(finalFieldname)).findFirst().get();
                        if(targetSetField!=null){
                            System.out.println("Updated field is annotated?:" + targetSetField.isAnnotationPresent(Column.class));
                            if (targetSetField.isAnnotationPresent(Column.class)) {
                                updatedFieldsMap.put(Arrays.asList(entityName,targetSetField.getDeclaredAnnotation(Column.class).name()), args);
                                LOG.debug(updatedFieldsMap.toString());
                            } else {
                                updatedFieldsMap.put(Arrays.asList(entityName,targetSetField.getName()), args);
                            }
                        }


            }

        }
        jp.proceed(args);
        System.out.println("Around after" );

    }

    private static boolean runAround = true;
    private  String kozak = "kozak";
    private Integer asd = 1;
    private int priv=0;

    public static void main(String[] args) {
        /*new TestAspect().hello();
        runAround = false;
        new TestAspect().hello();
        TestAspect a = new TestAspect();a.setKozak("gdfg");
        System.err.println("kozak    " + a.getKozak());
*/
        TestAspect a = new TestAspect();
        a.setKozak("kiii");
        a.setAsd(12);
        System.out.println("kozak is "+ a.getKozak()); System.out.println("asd is "+ a.getAsd());


        a.setKo("1","2");
        User u = new User("gg","ff","tt",Role.ROLE_USER);
        u.setEmail("Fdfd");
        u.setName("porka");
        UserMeal um = new UserMeal();
        um.setDescription("dfddf");
        UserMeal um2 = new UserMeal();
        um.setDescription("222");
        u.addAuthorities(Role.ROLE_ADMIN);
        u.setName("porka");
        u.setName("mapik");
        u.setEmail("changa");
       //так лучше не делать так как создаетсяя анонимный класс жрущий память
       u.setAuthorities(new HashSet<Role>(){{
                add(Role.ROLE_ADMIN);
                add(Role.ROLE_GUEST);}});

        u.setEnabled(true);
        u.setMeals(new ArrayList<>(Arrays.asList(um,um2)));
        /*
        ИТАК! Развязка! Меняя юзермил, менять набор у юзера не надо. он ссылаетсся на юзер мил,который сам обновится.
        ну не без моей помощи конечно.
         */
        um2.setDescription("umumum");
        u.setMeals(new ArrayList<>(Arrays.asList(um,um2)));
        u.setBorsch();
        u.getBorsch();
        System.out.println("email is "+ u.getEmail());
        Object[] oa = new Object[]{"asd"};

        //updatedFieldsMap.put("first",oa);

        System.out.println("From TESTMAP " + updatedFieldsMap.toString());


    }

    public static void fetchMap(Map<List<String>,? super Object> m){
        updatedFieldsMap = m;

    }
    public  String getKozak() {
        return kozak;
    }

    public  void setKozak(String kozak) {
        this.kozak = kozak;
    }

    public Integer getAsd() {
        return asd;
    }

    public void setAsd(Integer asd) {
        this.asd = asd;
    }

    public String setKo(String s1, String s2){
        System.out.println("s1+s2= " + s1+" "+ s2);
        return s1+s2;
    }
    public void hello() {

        System.err.println("in hello " + kozak);

    }

    //один срез. И 3 совета. у которых этот общий срез дополняется разным набором аргументов. класс
    @Pointcut("execution(* set*(..)) && target(TestAspect) && args(..)")
    public void pc2(){}

    @Around("pc2() && args(a)")
    public Object aroundSet(String a, ProceedingJoinPoint jp) throws Throwable {

        System.err.println("AROUND SET BEFORE " +jp.getSignature().getName()  );
        for(Object o : jp.getArgs()){
            System.err.println(" arg is "+ (String)o);
        }
        Object[] args = jp.getArgs();
        args[0]="str";
        String f="first_advice";
        //if(args.length>1) args[1]="ssssss";
        Object res = jp.proceed(args);
        System.err.println("AROUND SET After "+jp.getSignature().getName());
        for(Object o : args){
            System.err.println(" arg is "+ (String)o);
        }
        return res;


    }
    /*
    внедряются оба совета, однако. понял. просиид нужно вызывать в одном месте.
    мы не можем выполнить два раза один вызов. потому и получаем такое.
    @Around("pc2() && args(arg,*)") и в параметрах совета (String arg) можно так указывать
     */
    @Around("pc2() && args(c)")
    public Object aroundSet2(Integer c, ProceedingJoinPoint jp) throws Throwable {

        System.err.println("AROUND SET 2 BEFORE " +jp.getSignature().getName() );
        for(Object o : jp.getArgs()){
            System.err.println(" arg is "+ o.toString());
        }
        Object[] args = jp.getArgs();
        //args[0]="ds4322fs";
        if(args.length>1) args[1]="int";
        String t="second_advice";
        c=c*2;
        Object res = jp.proceed(new Object[]{c});
        System.err.println("AROUND SET 2 After " + jp.getSignature().getName());

            System.err.println(" arg is "+ c);

        return res;


    }

    @Around("pc2() && args(a,b)")
    public Object aroundSet3(String a,String b, ProceedingJoinPoint jp) throws Throwable {

        System.err.println("AROUND SET BEFORE " +jp.getSignature().getName()  );
        for(Object o : jp.getArgs()){
            System.err.println(" arg is "+ (String)o);
        }

        Object[] args = jp.getArgs();
        args[0]="str";
         a ="second_advice";
         b="first_advice";
        //if(args.length>1) args[1]="ssssss";
        Object res = jp.proceed(new Object[]{a,b});
        System.err.println("AROUND SET After "+jp.getSignature().getName());

            System.err.print(" arg a is "+ a);
        System.err.println(" arg b is "+ b);

        return res;


    }

    @After("execution(void ru.headrich.topjava.DAO.ORMengine.aspects.TestAspect.hello())")
    public void afterHello(JoinPoint joinPoint) {
        System.err.println("after " + joinPoint);
    }

    @Around("execution(void ru.headrich.topjava.DAO.ORMengine.aspects.TestAspect.hello())")
    public void aroundHello(ProceedingJoinPoint joinPoint) throws Throwable {
        System.err.println("in around before " + joinPoint);
        if (runAround) {

            joinPoint.proceed();
        }
        System.err.println("in around after " + joinPoint);
    }

    @Before("execution(void ru.headrich.topjava.DAO.ORMengine.aspects.TestAspect.hello())")
    public void beforeHello(JoinPoint joinPoint) {
        System.err.println("before " + joinPoint);
    }




}
