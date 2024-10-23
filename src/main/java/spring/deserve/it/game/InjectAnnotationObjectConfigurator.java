package spring.deserve.it.game;

import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withModifier;

public class InjectAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object obj) {
        // Обрабатываем поля
        configureFields(obj);

        // Обрабатываем методы
        configureMethods(obj);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void configureFields(Object obj) {
        // Используем Reflections для получения всех полей (включая родительские)
        Set<Field> fields = ReflectionUtils.getAllFields(obj.getClass(), withAnnotation(Inject.class));

        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            Object dependency = ObjectFactory.getInstance().createObject(fieldType);

            field.setAccessible(true);
            field.set(obj, dependency);  // Тут SneakyThrows обрабатывает IllegalAccessException
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void configureMethods(Object obj) {
        // Используем Reflections для получения всех методов (включая родительские)
        Set<Method> methods = ReflectionUtils.getAllMethods(obj.getClass(),
                withAnnotation(Inject.class),
                withModifier(java.lang.reflect.Modifier.PUBLIC));

        for (Method method : methods) {
            if (isSetter(method)) {
                Class<?> paramType = method.getParameterTypes()[0];
                Object dependency = ObjectFactory.getInstance().createObject(paramType);

                method.setAccessible(true);
                method.invoke(obj, dependency);  // SneakyThrows обрабатывает все исключения, такие как IllegalAccessException и InvocationTargetException
            }
        }
    }

    private boolean isSetter(Method method) {
        return method.getName().startsWith("set") && method.getParameterCount() == 1;
    }
}
