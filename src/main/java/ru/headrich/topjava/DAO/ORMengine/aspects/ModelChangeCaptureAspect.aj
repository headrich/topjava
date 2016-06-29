package ru.headrich.topjava.DAO.ORMengine.aspects;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.util.regexp.Regexp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import ru.headrich.topjava.DAO.DAOFactory;
import ru.headrich.topjava.DAO.ORMengine.annotations.Column;
import ru.headrich.topjava.DAO.ORMengine.annotations.Table;
import ru.headrich.topjava.DAO.SimpleDAOFactory;
import ru.headrich.topjava.DAO.UserDAOImpl;

import java.beans.Statement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Montana on 23.06.2016.
 */
@Aspect
public class ModelChangeCaptureAspect extends ModelAspect {
    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ModelChangeCaptureAspect.class);
//хранить имя таблицы, столбца и значение.
    //ключ -пара имя таблицы-имя столбца( или сущность-поле , еще не решил) , значение - установленное значение.
    //так удобней потому что не вставить лишние изменения таблица-столбец. всегда актуальн
    private static Map<List<String>,? super Object> updatedFieldsMap = new HashMap<>();
    //так бля, оказывается у нас то юзеров много,да и милсов тоже, и записав обновление одного милса, второй останется без него. не дело.

    public Map<List<String>, ? super Object> getUpdatedFieldsMap() {
        return updatedFieldsMap;
    }

    public void setUpdatedFieldsMap(Map<List<String>, ? super Object> updatedFieldsMap) {
        ModelChangeCaptureAspect.updatedFieldsMap = updatedFieldsMap;
    }

    public ModelChangeCaptureAspect() {
    }
    //для проверки изменений сразу
    public ModelChangeCaptureAspect(UserDAOImpl.StateHolder stateHolder) {

    }
    //если мы заранее замапили сущности, тут можно не страдать рефлекссией, а просто по филдпаттерну взять и мапы. (поле,столбец)
    public ModelChangeCaptureAspect(Map<? extends Object,String> columnFieldMap) {

    }

//из-за этого совета не работал другой, они все заранее компилируются и встраиваются байткодом,
//и так как это around и в нем ниче не выполняется, то у меня null возвращался.
    //интересно наблюдение что, почему-то именно этот совет исполнялся, а не другой, более конкретный по срезу с именем метода.
    //даже если пометить аннотацией. Видимо нужно грамонто расставлять точки среза, чтобы они не пересекались!
    //вот указав аннотацию там и исключив ее тут, все заробило.ШИК
    //САМО ОНО НЕ РАЗРЕШАЕТ ПЕРЕСЕЧЕНИЯ, ПРОСТО БОЛЕЕ ОБЩИЕ ПЕРЕКРЫВАЮТ КОНКРЕТНЫЕ
    @Around("setters()")//исключили User из среза
    public Object catchChanghesAdvice(ProceedingJoinPoint jp) throws Throwable {
        String methodname = jp.getSignature().getName();
        String getMethodname="";
        Object[] args = jp.getArgs();
        String targetString  = jp.getTarget().toString();
        LOG.debug("\nAround before "+jp.getKind()+": " +methodname+ " with arguments: "+ Arrays.toString(args)
                +" on target " +targetString);

        //args[0] = "Koreandr@gmail.lom";



        Object currentFieldValue=null;
        String fieldname="";
        String entityName;
        if(args!=null && args.length!=0){

            final String fieldPattern=methodname.substring(3).toLowerCase();
            //LOG.debug("Fieldpattern - " + fieldPattern);


            Class targetClass = jp.getTarget().getClass();

            //проверяем изменения. Если не отличается - ниче не делаем , если отличается от текущего - то устанавливаем и пихаем в карту обновлений.
            Method[] methods = targetClass.getMethods();
            Method getterMethod=null;
            for(Method m : methods){
                if(m.getName().matches("(?i)(has|get|is|should|can)("+fieldPattern+")") && m.getParameterCount()==0){
                    getterMethod=m;
                    //LOG.debug("GETTERMETHOD IS   " +getterMethod.toString());
                    currentFieldValue = getterMethod.invoke(jp.getTarget(),null);
                    LOG.debug("Reflection get current value :  " + fieldPattern +" = "+currentFieldValue);
                    break;
                }
            }

            if(args.length>0) {
                if(args.length==1){
                    if(args[0].equals(currentFieldValue)){
                        LOG.info("changes are not detected. "+fieldPattern+" : "+currentFieldValue);
                        return null;
                    }
                }else{
                    if(Arrays.asList(args).equals(currentFieldValue)){
                        LOG.info("changes are not detected. "+fieldPattern+" : "+currentFieldValue);
                    }
                    //collections? ...variable number
                    //массив kak и переменное число аргументов это T...values
                }
            }



            //аннотированые сущности берем ли все?
            /*
            Возможно тут стоит собрать не имя столбца, а поле и значение. А определить столбец уже позже.
            т.к. надо будет определить что если это поле - не столбец в этой таблице, а связь по ключу..
            в общем логика требует проверки поля, на релефантность, а это не должно быть тут.
            Тут мы можем сравнить изменилось ли значение, и если да -то записать на обновление в мапу.
            А уж что как и куда, это в другом месте. ОК
             */
            if(targetClass.isAnnotationPresent(Table.class)) {
                entityName = jp.getTarget().getClass().getDeclaredAnnotation(Table.class).name();
            }else{
                entityName = jp.getTarget().getClass().getSimpleName();
            }
                        //targetClass.getDeclaredAnnotation(Table.class).name();


                ArrayList<Field> afl = new ArrayList<>();
                Class c = targetClass;
                //собираем все поля.
                while(c!=null && !c.equals(Object.class)){

                    //System.out.println(c.getName());
                    Collections.addAll(afl, c.getDeclaredFields());
                    c = c.getSuperclass();

                }

                Field targetSetField = afl.stream().filter(f->f.getName().matches("(?i)("+fieldPattern+")")).findFirst().orElse(null);
                //getter for field = getFiledname or isFieldname-> get and call it by  a)PropertyUtils b)Reflection: find by fieldname c)Spring BeanWrapper
                    /*LOG.debug("PU get " +PropertyUtils.getProperty(jp.getTarget(),targetSetField.getName()));
                LOG.debug("BW get " + new BeanWrapperImpl(jp.getTarget()).getPropertyValue(targetSetField.getName()));
                LOG.debug("BU get " + BeanUtils.getProperty(jp.getTarget(),targetSetField.getName()));*/




                //if !securityManager
                //targetSetField.setAccessible(true);
//                LOG.debug(targetSetField.get(targetString).toString());
                //System.out.println("targetField - " + targetSetField);
                if(targetSetField!=null){
                   LOG.debug("Updated field is annotated?:" + targetSetField.isAnnotationPresent(Column.class));
                    if (targetSetField.isAnnotationPresent(Column.class)) {
                        updatedFieldsMap.put(Arrays.asList(entityName,targetSetField.getDeclaredAnnotation(Column.class).name()), args);

                    } else {
                        updatedFieldsMap.put(Arrays.asList(entityName,targetSetField.getName()), args);
                    }
                    LOG.debug(" MAP    " +updatedFieldsMap.toString());
                }



        }
        //SimpleDAOFactory.getInstance().getUserDAO().n.fetchMap(updatedFieldsMap);
        //UserDAOImpl.StateHolder.fetchMap(updatedFieldsMap);
        TestAspect.fetchMap(updatedFieldsMap);
        return jp.proceed(args);


    }








}
