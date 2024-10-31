package org.supercompany.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(value = "debug", havingValue = "true")
public class CustomVersionConfiguration {
    @Bean
    public CustomVersion customVersion(String superCompanyCoreVersion) {
        return new CustomVersion();
    }
}
