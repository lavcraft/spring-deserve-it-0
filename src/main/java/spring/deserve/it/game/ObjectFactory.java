package spring.deserve.it.game;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class ObjectFactory {

    // Единственный экземпляр фабрики (Singleton)
    private static ObjectFactory instance;

    // Набор конфигураторов
    private final Set<ObjectConfigurator> configurators;

    // Закрытый конструктор, чтобы запретить создание экземпляров извне
    @SneakyThrows
    private ObjectFactory() {
        // Инициализация конфигураторов при создании фабрики
        Reflections reflections = new Reflections("spring.deserve.it");  // Укажите ваш пакет
        Set<Class<? extends ObjectConfigurator>> configuratorClasses = reflections.getSubTypesOf(ObjectConfigurator.class);

        configurators = configuratorClasses.stream()
                .map(this::createInstance)
                .collect(Collectors.toSet());
    }

    // Метод для ленивой инициализации и получения экземпляра фабрики
    public static ObjectFactory getInstance() {
        if (instance == null) {
            synchronized (ObjectFactory.class) {
                if (instance == null) { // Double-checked locking для потокобезопасности
                    instance = new ObjectFactory();
                }
            }
        }
        return instance;
    }

    // Метод для создания экземпляра объекта по классу
    @SneakyThrows
    public <T> T createObject(Class<T> clazz) {
        T obj = clazz.getDeclaredConstructor().newInstance();

        // Применяем все конфигураторы
        for (ObjectConfigurator configurator : configurators) {
            configurator.configure(obj);
        }

        return obj;
    }

    // Вспомогательный метод для создания экземпляров конфигураторов
    @SneakyThrows
    private ObjectConfigurator createInstance(Class<? extends ObjectConfigurator> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }
}
