package org.supercompany.spyders;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(SpiderAlertProperties.class)
@ConditionalOnProperty(prefix = "spider.alert", name = {"remaining-life", "alert-email"})
public class SpyderStarterConf {
    @Bean
    public MailSender mailSender(SpiderAlertProperties alertProperties) {
        return new MailSender(alertProperties);
    }
    @Bean
    public SpiderBeanPostProcessor spiderBeanPostProcessor(){
        return new SpiderBeanPostProcessor();
    }

}
