package spring.deserve.it.game;

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

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

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

        return obj;
    }

    @SneakyThrows
    private ObjectConfigurator createInstance(Class<? extends ObjectConfigurator> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }

    // Метод для получения контекста
    public ApplicationContext getContext() {
        return context;
    }
}
