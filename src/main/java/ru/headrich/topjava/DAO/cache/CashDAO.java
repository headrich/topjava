package ru.headrich.topjava.DAO.cache;

import org.ehcache.Cache;
import ru.headrich.topjava.DAO.CommonDAO;
import ru.headrich.topjava.DAO.ORMengine.aspects.EntityImplementator;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Montana on 27.06.2016.
 */

//вся логика подключения к этому хранилищу будет конфиг да инит . стоит ли выносить в отдельное место хз
    //интеренсо бы найти подходящий паттерн, котоырй бы разделил логику подключения и логику манипуляций с данными
    //всякие там наследования,делегирвоания...
public class CashDAO<T extends EntityImplementator.Entity,PK  extends Serializable> implements CommonDAO<T ,PK>{

    //@Component? @Autowired? @Repository?  -  injection by Spring Framework. Coming Soon!
    Cache persistentsCache;




    protected Cache getPersistentsCache() {
        return persistentsCache;
    }
    //ну а без спринга. Инъекция будет так.
    protected void setPersistentsCache(Cache persistentsCache) {
        this.persistentsCache = persistentsCache;
    }



    @Override
    public T getById(int id) throws SQLException {
        persistentsCache.get(id);
        return null;
    }


    //работаем с сущностями через интерфейс Entity /так как мы(компилер) не знаем заранее что это за модели данных.
    //потому мы либо вручную (как кислин) наследуем наши сущности от BaseEntity.
    // Но так как я не Гриша, и делаю суперфреймворк в учебных целях, нам нужно в рантайме создавать прокси-копии объектов сущностей клиента(Ивана)
    // и при этом они должны реализовывать Entity интерфейс..
    // В итоге мы получаем возможность управлять этими сущностями в полной мере и работать с ними в дао кеша и дао бд,
    // причем так что если не одно то другое дао использвем, и все проверки реализуем в аспектах. Шикарно получится.
    //надо переварить товрог)
    @Override
    public T save(T entity) {

        persistentsCache.put(entity.getId().intValue(),entity.toCacheString()); // такс

        return null;
    }

    @Override
    public List<T> getAll(Class<T> clazz) {
        return null;
    }

    @Override
    public void update(T entity) {

    }




    public <V>List getAlls(Class<V> v) {
        return (List) persistentsCache.getAll(new HashSet<V>()).values();
    }


    @Override
    public boolean delete(int id) {
        return false;
    }
}
