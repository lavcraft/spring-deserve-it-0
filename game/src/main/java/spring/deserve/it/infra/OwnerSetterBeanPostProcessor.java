package spring.deserve.it.infra;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import spring.deserve.it.api.Spider;
import spring.deserve.it.game.PlayerQualifier;

@Component
public class OwnerSetterBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof Spider spider){
            String playerName = bean.getClass().getAnnotation(PlayerQualifier.class).value();
            spider.setOwner(playerName);
        }

        return bean;
    }
}
