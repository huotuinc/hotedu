package com.huotu.hotedu.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.huotu.hotedu.model.ICommonEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CJ
 */
public class EnumJsonSerializer implements Converter<ICommonEnum,Map> {

    @Override
    public Map convert(ICommonEnum value) {
        HashMap<String,Object> map = new HashMap();
        map.put("name",value.getName());
        map.put("value",value.getValue());
        return map;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.uncheckedSimpleType(ICommonEnum.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructMapLikeType(Map.class,String.class,Object.class);
    }
}