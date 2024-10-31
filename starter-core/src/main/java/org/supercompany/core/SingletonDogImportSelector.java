package org.supercompany.core;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.reflections.Reflections;

import java.util.Set;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SingletonDogImportSelector implements ImportBeanDefinitionRegistrar {

    private Environment env;

    public SingletonDogImportSelector(Environment environment) {
        this.env = environment;
    }

    private static final String PROPERTY_KEY = "importsingleton";

    @Override
    @SneakyThrows
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String propertyValue = env.getProperty(PROPERTY_KEY);

        List<String> packagesToScan = new ArrayList<>();

        // 1. Проверяем наличие свойства importsingleton в application.yaml
        if (propertyValue != null && !propertyValue.isBlank()) {
            // Если есть, добавляем пакеты из проперти
            String[] packages = propertyValue.split(",");
            for (String pkg : packages) {
                packagesToScan.add(pkg.trim());
            }
        } else {
            // 2. Если свойства нет, находим главный конфигурационный класс
            String mainConfigClass = findMainConfigClass(registry);
            if (mainConfigClass == null) {
                return;
//                throw new IllegalStateException("Центральная конфигурация с аннотацией @SpringBootApplication не найдена.");
            }

            // Добавляем пакет главной конфигурации
            Class<?> configClass = Class.forName(mainConfigClass);
            packagesToScan.add(configClass.getPackage().getName());
        }

        // 3. Сканируем все указанные пакеты и регистрируем бины с аннотацией @Singleton
        for (String basePackage : packagesToScan) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> singletonClasses = reflections.getTypesAnnotatedWith(Singleton.class);

            for (Class<?> singletonClass : singletonClasses) {
                registerSingletonBean(singletonClass, registry);
            }
        }
    }

    @SneakyThrows
    private String findMainConfigClass(BeanDefinitionRegistry registry) {
        for (String beanName : registry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();

            if (beanClassName != null) {
                Class<?> beanClass = Class.forName(beanClassName);

                // Проверка на наличие @SpringBootApplication
                if (beanClass.isAnnotationPresent(SpringBootApplication.class)) {
                    return beanClassName;
                }
            }
        }
        return null;
    }

    private void registerSingletonBean(Class<?> singletonClass, BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(singletonClass);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        String beanName = singletonClass.getName();
        registry.registerBeanDefinition(beanName, beanDefinition);

        System.out.println("Зарегистрирован singleton-бин: " + beanName);
    }
}
