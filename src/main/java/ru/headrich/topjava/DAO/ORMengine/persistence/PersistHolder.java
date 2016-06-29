package ru.headrich.topjava.DAO.ORMengine.persistence;


import org.ehcache.*;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.Ehcache;
import org.ehcache.core.config.ResourcePoolsImpl;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by Montana on 27.06.2016.
 */
public class PersistHolder  {

    CacheManager cacheManager;
    //Cache c;
    public <T>Object get(T obj){
     return null;
    }

    public <T>Object add(T obj){

        return  null;
    }

    //или же сделать это так/
    // чтобы наш персистхолдер использовал наше же дао, но только не к базе, а
    // а с локальным хранилищем/. Задумка очень интересна
    //понять бы как хранить на диске,А не только в памяти.
    //у хибера кстати есть несколько урвоней кеша, один как я хотел - общий на все,
    // другой = только на сессию, а у меня типо 1 сессия будет на поток, чтобы не антипаттернил иван.
    // ну и третий- кэш запросов( на всякие там crateQuery  разные  кэшируется) - это я тоже
    // обдумывал. Оно у меня все в одном кэше. А вот как именно хранить запросики - это у хибера спизжу.
    // получается я мыслю абстрактно больше, архитектурно, а не реализацией,
    // в этом есть некий плюс постижения
    // мастерства ООП. да и это упрощает создание крупных преоктов. когда видишь че как интерфейсами, в общем плане так сказать, а не детальками.

    //но можно использовать готовые кэшы.

    private <K,V>Cache<K,V> initCache(Class<K> k, Class<V> v){


        CacheConfiguration cacheConfiguration  = CacheConfigurationBuilder.newCacheConfigurationBuilder(k,v, ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(10, EntryUnit.ENTRIES)
                .offheap(1, MemoryUnit.MB)).build();
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("cache1",cacheConfiguration)
                .withDefaultSizeOfMaxObjectGraph(1000).withDefaultSizeOfMaxObjectSize(1,MemoryUnit.KB).build();
        cacheManager.init();

        Cache<K,V> c = cacheManager.getCache("cache1",k,v);



        return c;
    }


}
