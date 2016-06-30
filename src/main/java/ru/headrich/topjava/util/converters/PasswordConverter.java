package ru.headrich.topjava.util.converters;

import javax.persistence.AttributeConverter;

/**
 * Created by Montana on 30.06.2016.
 */
public class PasswordConverter implements AttributeConverter<String,String>{
    @Override
    public String convertToDatabaseColumn(String s) {
        try {
            return PasswordEncryption.getEncryptor().getHash(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return s;
    }
}
