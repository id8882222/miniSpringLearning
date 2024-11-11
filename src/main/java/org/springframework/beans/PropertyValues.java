package org.springframework.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {

    private  final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void  addPropertyValue(PropertyValue pv){
        propertyValueList.add(pv);
    }

    public PropertyValue[] getPropertiesValues(){
        PropertyValue[] propertyValues = new PropertyValue[0];
        return this.propertyValueList.toArray(propertyValues);
    }

    public PropertyValue getPropertyValue(String propertyName){
        for(int i = 0; i < this.propertyValueList.size(); i++){
            PropertyValue pv = this.propertyValueList.get(i);
            if(pv.getName().equals(propertyName)){
                return pv;
            }
        }
        return null;
    }

}
