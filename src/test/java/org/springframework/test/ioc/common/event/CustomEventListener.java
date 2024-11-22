package org.springframework.test.ioc.common.event;

import org.springframework.context.ApplicationListener;

public class CustomEventListener implements ApplicationListener<CustomEvent> {
    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println(this.getClass().getName() + ":: 我听到了-自定义监听器-开始执行我的事件");
    }
}
