package spring.deserve.it.game;

import lombok.Setter;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withModifier;

import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withModifier;

public class InjectAnnotationObjectConfigurator implements ObjectConfigurator {

    // Контекст, из которого мы будем брать зависимости
    @Setter
    private ApplicationContext applicationContext;

    // Сеттер для контекста

    @Override
    public void configure(Object obj) {
        configureFields(obj);
        configureMethods(obj);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void configureFields(Object obj) {
        Set<Field> fields = ReflectionUtils.getAllFields(obj.getClass(), withAnnotation(Inject.class));

        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            Object dependency = applicationContext.getBean(fieldType);  // Получаем зависимость из контекста

            field.setAccessible(true);
            field.set(obj, dependency);
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void configureMethods(Object obj) {
        Set<Method> methods = ReflectionUtils.getAllMethods(obj.getClass(),
                withAnnotation(Inject.class),
                withModifier(java.lang.reflect.Modifier.PUBLIC));

        for (Method method : methods) {
            if (isSetter(method)) {
                Class<?> paramType = method.getParameterTypes()[0];
                Object dependency = applicationContext.getBean(paramType);  // Получаем зависимость из контекста

                method.setAccessible(true);
                method.invoke(obj, dependency);
            }
        }
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set") && method.getParameterCount() == 1;
    }
}
