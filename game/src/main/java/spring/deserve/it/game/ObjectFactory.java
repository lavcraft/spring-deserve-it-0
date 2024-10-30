package spring.deserve.it.game;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.reflections.*;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.withAnnotation;

import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import spring.deserve.it.infra.ProxyConfigurator;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.withAnnotation;

public class ObjectFactory {

    private final ApplicationContext context;
    private final Set<ObjectConfigurator> configurators;
    private final Set<ProxyConfigurator> proxyConfigurators;

    @SneakyThrows
    public ObjectFactory(ApplicationContext context) {
        this.context = context;

        Reflections reflections = context.getReflections();

        // Ищем все конфигураторы объектов
        Set<Class<? extends ObjectConfigurator>> configuratorClasses = reflections.getSubTypesOf(ObjectConfigurator.class);
        configurators = configuratorClasses.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());

        // Ищем все ProxyConfigurator
        Set<Class<? extends ProxyConfigurator>> proxyConfiguratorClasses = reflections.getSubTypesOf(ProxyConfigurator.class);
        proxyConfigurators = proxyConfiguratorClasses.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    public <T> T createObject(Class<T> clazz) {
        // Создаем объект через конструктор
        T obj = clazz.getDeclaredConstructor().newInstance();

        // Применяем конфигураторы
        configure(obj);

        // Вызов @PostConstruct перед оборачиванием в прокси
        invokePostConstruct(obj);

        // Применяем прокси (если требуется)
        obj = wrapWithProxyIfNeeded(clazz, obj);

        return obj;
    }

    private <T> T wrapWithProxyIfNeeded(Class<T> clazz, T obj) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            obj = proxyConfigurator.wrapWithProxy(obj, clazz);
        }
        return obj;
    }

    private <T> void configure(T obj) {
        for (ObjectConfigurator configurator : configurators) {
            configurator.setApplicationContext(context);
            configurator.configure(obj);
        }
    }

    @SneakyThrows
    private <T> void invokePostConstruct(T obj) {
        Set<Method> postConstructMethods = ReflectionUtils.getAllMethods(obj.getClass(), withAnnotation(PostConstruct.class));

        for (Method method : postConstructMethods) {
            method.setAccessible(true);
            method.invoke(obj);
        }
    }

    @SneakyThrows
    private <T> T createInstance(Class<T> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }
}
