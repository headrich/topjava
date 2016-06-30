package ru.headrich.topjava.util.converters;

import com.sun.istack.internal.Nullable;

/**
 * Created by Montana on 30.06.2016.
 */
public interface PasswordEncryptor {
    String getHash(String password) throws Exception;
    boolean authenticate(String password,String hash) throws Exception;
    String decode(String hash, @Nullable String salt);

}
