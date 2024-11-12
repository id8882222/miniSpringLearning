package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader{
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    @Override
    public Resource getResource(String location) {
        //classpath下的资源
        if(location.startsWith(CLASSPATH_URL_PREFIX)){
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }else{
            try{
                //尝试当做url来处理
                URL url = new URL(location);
                return new UrlResource(url);
            }catch (MalformedURLException ex){
                //当做文件系统下面的资源处理
                return new FileSystemResource(location);
            }
        }
    }
}
