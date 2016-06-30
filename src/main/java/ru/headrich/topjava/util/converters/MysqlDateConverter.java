package ru.headrich.topjava.util.converters;

import javax.persistence.AttributeConverter;
import java.sql.Date;

/**
 * Created by Montana on 30.06.2016.
 */
public class MysqlDateConverter implements AttributeConverter<java.util.Date,java.sql.Date> {

    @Override
    public Date convertToDatabaseColumn(java.util.Date date) {
        return new Date(date.getTime());
    }

    @Override
    public java.util.Date convertToEntityAttribute(Date date) {
        return new java.util.Date(date.getTime());
    }


}
