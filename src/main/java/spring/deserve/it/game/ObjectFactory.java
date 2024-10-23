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

public class ObjectFactory {

    // Контекст, с которым работает фабрика
    private final ApplicationContext context;

    // Набор конфигураторов
    private final Set<ObjectConfigurator> configurators;

    @SneakyThrows
    public ObjectFactory(ApplicationContext context) {
        this.context = context;

        // Получаем Reflections из контекста и сканируем пакет для поиска ObjectConfigurator
        Reflections reflections = context.getReflections();
        Set<Class<? extends ObjectConfigurator>> configuratorClasses = reflections.getSubTypesOf(ObjectConfigurator.class);

        configurators = configuratorClasses.stream()
                .map(this::createInstance)
                .peek(configurator -> configurator.setApplicationContext(context))
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    public <T> T createObject(Class<T> clazz) {
        T obj = clazz.getDeclaredConstructor().newInstance();

        // Применяем все конфигураторы, передавая им контекст
        for (ObjectConfigurator configurator : configurators) {
          // Передаем контекст через сеттер
            configurator.configure(obj);
        }
        invokePostConstruct(obj);

        return obj;
    }

    @SneakyThrows
    private ObjectConfigurator createInstance(Class<? extends ObjectConfigurator> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private <T> void invokePostConstruct(T obj) {
        // Ищем все методы, помеченные @PostConstruct
        Set<Method> postConstructMethods = ReflectionUtils.getAllMethods(obj.getClass(), withAnnotation(PostConstruct.class));

        // Вызываем каждый метод
        for (Method method : postConstructMethods) {
            method.setAccessible(true);
            method.invoke(obj);  // SneakyThrows обрабатывает исключения
        }
    }

    // Метод для получения контекста
    public ApplicationContext getContext() {
        return context;
    }
}
