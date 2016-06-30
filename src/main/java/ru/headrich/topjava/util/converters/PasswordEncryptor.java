package ru.headrich.topjava.util.converters;

/**
 * Created by Montana on 30.06.2016.
 */
public interface PasswordEncryptor {
    String getHash(String password) throws Exception;
    boolean authenticate(String password,String salt) throws Exception;
}
