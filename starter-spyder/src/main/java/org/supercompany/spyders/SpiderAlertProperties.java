package org.supercompany.spyders;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "spider.alert")
public class SpiderAlertProperties {
    private Integer remainingLife;
    private String alertEmail;

}
