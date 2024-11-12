package org.springframework.test;

import cn.hutool.core.io.IoUtil;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.in;

public class ResourceAndResourceLoaderTest {
    @Test
    public void testResourceLoader() throws IOException {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        //加载ClassPath下面的资源
        Resource resource = resourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        assertThat(content).isEqualTo("hello world");

        //加载文件系统资源
        resource = resourceLoader.getResource("C:\\Users\\pc\\Desktop\\hello.txt");
        assertThat(resource instanceof FileSystemResource).isTrue();
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        assertThat(content).isEqualTo("hello world");

        //加载url资源
        resource = resourceLoader.getResource("https://www.baidu.com");
        assertThat(resource instanceof UrlResource).isTrue();
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);

    }
}
