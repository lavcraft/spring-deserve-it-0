package org.supercompany.core;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({SingletonDogImportSelector.class, DemoRegistrar.class})
public class SuperCompanyCoreAutoConfiguration {
    @Bean
    public String superCompanyCoreVersion() {
        return new String("1.0.0");
    }


    @Bean
    public BenchmarkBPP benchmarkBpp() {
        return new BenchmarkBPP();
    }

}
