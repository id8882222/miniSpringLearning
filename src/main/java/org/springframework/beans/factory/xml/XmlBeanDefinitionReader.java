package org.springframework.beans.factory.xml;


import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.apache.naming.factory.ResourceEnvFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.exceptions.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    public static final String COMPONENT_SCAN_ELEMENT = "component-scan";

    public static final String LAZYINIT_ATTRIBUTE = "lazy-init";

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }


    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try{
            InputStream inputStream = resource.getInputStream();
            try {
                doLoadBeanDefinitions(inputStream);
            }finally {
                inputStream.close();
            }
        } catch (IOException | DocumentException ex) {
            throw new BeansException("IOException parsing XML document from " + resource, ex);
        }
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws DocumentException{
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);

        Element root = document.getRootElement();

        Element componentScan = root.element(COMPONENT_SCAN_ELEMENT);
        if(componentScan != null){
            String scanPath = componentScan.attributeValue(BASE_PACKAGE_ATTRIBUTE);
            if(StrUtil.isEmpty(scanPath)){
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(scanPath);
        }

        List<Element> beanList = root.elements(BEAN_ELEMENT);
        for(Element bean : beanList){
            String id = bean.attributeValue(ID_ATTRIBUTE);
            String name = bean.attributeValue(NAME_ATTRIBUTE);
            String className = bean.attributeValue(CLASS_ATTRIBUTE);
            String initMethodName = bean.attributeValue(INIT_METHOD_ATTRIBUTE);
            String destroyMethodName = bean.attributeValue(DESTROY_METHOD_ATTRIBUTE);
            String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);
            String lazyInit = bean.attributeValue(LAZYINIT_ATTRIBUTE);
            Class<?> clazz = null;

            try{
                clazz = Class.forName(className);
            }catch (ClassNotFoundException e){
                throw new BeansException("Cannot find class [" + className + "]");
            }
//            id优先于name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if(StrUtil.isEmpty(beanName)){
                //如果id和name都为空，将类名的第一个字母转为小写后 作为bean的名称;
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);
            beanDefinition.setLazyInit(Boolean.parseBoolean(lazyInit));
            if (StrUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }

            List<Element> propertyList = bean.elements(PROPERTY_ELEMENT);
            for(Element property : propertyList){
                String propertyNameAttribute = property.attributeValue(NAME_ATTRIBUTE);
                String propertyValueAttribute = property.attributeValue(VALUE_ATTRIBUTE);
                String propertyRefAttribute = property.attributeValue(REF_ATTRIBUTE);
                if (StrUtil.isEmpty(propertyNameAttribute)) {
                    throw new BeansException("The name attribute cannot be null or empty");
                }

                Object value = propertyValueAttribute;
                if (StrUtil.isNotEmpty(propertyRefAttribute)) {
                    value = new BeanReference(propertyRefAttribute);
                }
                PropertyValue propertyValue = new PropertyValue(propertyNameAttribute, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {
                //beanName不能重名
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            //注册BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
//        // 使用 XmlUtil 读取输入流并将其解析为 Document 对象
//        Document document = XmlUtil.readXML(inputStream);
//        // 获取 XML 文档的根元素
//        Element root = document.getDocumentElement();
//
//        // 获取名为 COMPONENT_SCAN_ELEMENT 的子元素
//        NodeList nodeList = root.getElementsByTagName(COMPONENT_SCAN_ELEMENT);
//        Element componentScan = (Element) nodeList.item(0);  // 获取第一个匹配的元素
//        if(componentScan != null){
//            String scanPath = componentScan.getAttribute(BASE_PACKAGE_ATTRIBUTE);
//            if(StrUtil.isEmpty(scanPath)){
//                throw new BeansException("The value of base-package attribute can not be empty or null");
//            }
//            scanPackage(scanPath);
//        }
//
//
//        // 获取根元素的所有子节点
//        NodeList childNodes = root.getChildNodes();
//        for(int i = 0; i < childNodes.getLength(); i++){
//            if(childNodes.item(i) instanceof Element){
//                if(BEAN_ELEMENT.equals(childNodes.item(i).getLocalName())){
//                    Element bean = (Element) childNodes.item(i);
//                    String id = bean.getAttribute(ID_ATTRIBUTE);
//                    String name = bean.getAttribute(NAME_ATTRIBUTE);
//                    String className = bean.getAttribute(CLASS_ATTRIBUTE);
//                    String initMethodName = bean.getAttribute(INIT_METHOD_ATTRIBUTE);
//                    String destroyMethodName = bean.getAttribute(DESTROY_METHOD_ATTRIBUTE);
//                    String beanScope = bean.getAttribute(SCOPE_ATTRIBUTE);
////                    System.out.println("id:" + id + " name: " + name + " className:" + className);
//                    Class<?> clazz = null;
//                    try{
//                        clazz = Class.forName(className);
//                    } catch (ClassNotFoundException e) {
//                        throw new BeansException("Cannot find class [" + className + "]");
//                    }
//                    //id优先于name
//                    String beanName = StrUtil.isNotEmpty(id) ? id : name;
//                    if(StrUtil.isEmpty(beanName)){
//                        //如果id和name都为空，将类名的第一个字母转为小写后 作为bean的名称;
//                        beanName = StrUtil.lowerFirst(clazz.getSimpleName());
//                    }
//
//                    BeanDefinition beanDefinition = new BeanDefinition(clazz);
//                    beanDefinition.setInitMethodName(initMethodName);
//                    beanDefinition.setDestroyMethodName(destroyMethodName);
//                    if (StrUtil.isNotEmpty(beanScope)) {
//                        beanDefinition.setScope(beanScope);
//                    }
//                    for(int j = 0; j < bean.getChildNodes().getLength(); j++){
//                        if(bean.getChildNodes().item(j) instanceof Element){
//                            if (PROPERTY_ELEMENT.equals(bean.getChildNodes().item(j).getNodeName())) {
//                                //解析property标签
//                                Element property = (Element) bean.getChildNodes().item(j);
//                                String nameAttribute = property.getAttribute(NAME_ATTRIBUTE);
//                                String valueAttribute = property.getAttribute(VALUE_ATTRIBUTE);
//                                String refAttribute = property.getAttribute(REF_ATTRIBUTE);
//
//                                if(StrUtil.isEmpty(nameAttribute)){
//                                    throw new BeansException("The name attribute cannot be null or empty");
//                                }
//
//                                Object value = valueAttribute;
//                                if(StrUtil.isNotEmpty(refAttribute)){
//                                    value = new BeanReference(refAttribute);
//                                }
//                                PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
//                                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
//                            }
//                        }
//                    }
//                    if(getRegistry().containsBeanDefinition(beanName)){
//                        //beanName不能重复
//                        throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
//                    }
//                    //注册BeanDefinition
//                    getRegistry().registerBeanDefinition(beanName, beanDefinition);
//                }
//            }
//        }
    }
    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    /**
     * 扫描注解Component的类，提取信息，组装成BeanDefinition
     * @param scanPath
     */
    private void scanPackage(String scanPath){
        String[] basePackages = StrUtil.splitToArray(scanPath, ",");
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }
}
