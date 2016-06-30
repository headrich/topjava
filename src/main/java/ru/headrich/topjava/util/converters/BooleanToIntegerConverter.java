package ru.headrich.topjava.util.converters;

import javax.persistence.AttributeConverter;

/**
 * Created by Montana on 30.06.2016.
 */
public class BooleanToIntegerConverter implements AttributeConverter<Boolean,Integer>{

    @Override
    public Integer convertToDatabaseColumn(Boolean aBoolean) {
        return aBoolean ? 1:0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer integer) {
        return integer!=0;
    }
}
