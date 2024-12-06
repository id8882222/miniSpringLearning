package org.springframework.test.ioc.common;

import org.springframework.core.convert.converter.Converter;

public class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
