package org.springframework.test.ioc.common;

import cn.hutool.db.handler.HandleHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;

import java.util.HashSet;
import java.util.Set;

public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

    @Override
    public Set<?> getObject() throws BeansException {
        HashSet<Object> converters = new HashSet<>();
        StringToLocalDateConverter stringToLocalDateConverter = new StringToLocalDateConverter("yyyy-MM-dd");
        converters.add(stringToLocalDateConverter);
        return converters;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
