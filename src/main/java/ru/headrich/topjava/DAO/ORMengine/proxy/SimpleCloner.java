package ru.headrich.topjava.DAO.ORMengine.proxy;

import java.io.*;

/**
 * Created by Montana on 21.06.2016.
 */
public class SimpleCloner {



    public static  <T> T generateClone(T object) throws IOException, ClassNotFoundException {
        T clone = null;
        try(ByteArrayOutputStream baos  = new ByteArrayOutputStream()){
            try(ObjectOutputStream oos = new ObjectOutputStream(baos);){
                oos.writeObject(object);
                try(ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())){
                    try(ObjectInputStream ois = new ObjectInputStream(bais)){
                        clone = (T)ois.readObject();
                    }
                }
            }
        }

        return clone;
    }

    private <T> T DecorateClone(T clone){
        //TODO добавить отслеживание вызовов методов.


        return clone;
    }
}
