package spring.deserve.it.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import org.reflections.Reflections;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.supercompany.spyders.api.Spider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpyderCountEnvironmentPostProcessor implements EnvironmentPostProcessor {
    SpringFactoriesLoader springFactoriesLoader;

    private static final String GOD_MOD_PROPERTY_NAME = "GodMod";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // Используем Reflections для поиска классов, реализующих интерфейс Spyder
        Reflections reflections = new Reflections(application.getMainApplicationClass().getPackageName());
        Set<Class<? extends Spider>> spyderClasses = reflections.getSubTypesOf(Spider.class);

        if (spyderClasses.size() > 20) {
            // Если количество классов больше 5, добавляем свойство GodMod=true
            Map<String, Object> godModProps = new HashMap<>();
            godModProps.put(GOD_MOD_PROPERTY_NAME, "true");
            environment.getPropertySources().addLast(new MapPropertySource("godModProperties", godModProps));
        }
    }
}
