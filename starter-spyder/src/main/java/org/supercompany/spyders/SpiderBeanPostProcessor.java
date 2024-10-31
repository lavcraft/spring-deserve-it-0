package org.supercompany.spyders;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.aop.framework.ProxyFactory;
import org.supercompany.spyders.api.Spider;

public class SpiderBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private MailSender mailSender;


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof Spider) || !mailSender.isAlertEnabled()) {
            return bean;
        }

        Spider spider = (Spider) bean;
        ProxyFactory factory = new ProxyFactory(spider);
        factory.addAdvice((org.aopalliance.intercept.MethodInterceptor) invocation -> {
            if (invocation.getMethod().getName().equals("loseLife")) {
                int remainingLife = spider.getLives();
                invocation.proceed();
                if (remainingLife <= mailSender.getAlertThreshold()) {
                    mailSender.sendAlert(
                            spider.getOwner(),
                            spider.getClass().getSimpleName(),
                            spider.hashCode(),
                            remainingLife
                    );
                }
                return null;
            }
            return invocation.proceed();
        });

        return factory.getProxy();
    }
}
