package spring.deserve.it.game;

import org.reflections.Reflections;
import org.supercompany.core.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    // Кеш для singleton-объектов
    private final Map<Class<?>, Object> singletonCache = new HashMap<>();

    // Фабрика для создания объектов
    private final ObjectFactory objectFactory;

    // Reflections для сканирования указанных пакетов
    private final Reflections reflections;

    // Конструктор принимает пакет для сканирования
    public ApplicationContext(String packageToScan) {
        // Инициализация Reflections для указанного пакета
        this.reflections = new Reflections(packageToScan);

        // Инициализация фабрики, которая будет работать с этим контекстом
        this.objectFactory = new ObjectFactory(this);
    }

    // Метод для получения объекта из контекста
    public <T> T getBean(Class<T> type) {
        // Проверяем, помечен ли класс аннотацией @Singleton
        Class<? extends T> clazz;

        if (type.isInterface()) {
            Set<Class<? extends T>> subTypesOf = reflections.getSubTypesOf(type);
            if (subTypesOf.isEmpty() || subTypesOf.size() > 1) {
                throw new IllegalStateException("no impl found, or more than one for ifc "+type);
            }
            clazz = subTypesOf.iterator().next();
        }else {
            clazz= type;
        }

        if (clazz.isAnnotationPresent(Singleton.class)) {
            // Если объект уже есть в кеше, возвращаем его
            if (singletonCache.containsKey(clazz)) {
                return (T) singletonCache.get(clazz);
            }
            // Создаем объект, если его нет, и сохраняем в кеше
            T obj = objectFactory.createObject(clazz);
            singletonCache.put(clazz, obj);
            return obj;
        } else {
            // Если класс не помечен @Singleton, создаем объект без кеширования
            return objectFactory.createObject(clazz);
        }
    }

    // Предоставляем доступ к Reflections для фабрики
    public Reflections getReflections() {
        return reflections;
    }
}
