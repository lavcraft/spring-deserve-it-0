package spring.deserve.it.infra;

import org.reflections.ReflectionUtils;
import spring.deserve.it.game.InjectProperty;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;

public class PropertyConfigurator {

    private final Properties properties = new Properties();


    public PropertyConfigurator() {
        // Загружаем application.properties из ресурсов
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("Файл application.properties не найден в ресурсах");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void configure(Object object) {
        Class<?> clazz = object.getClass();

        // Рекурсивно получаем все поля, включая из суперклассов, с аннотацией @InjectProperty
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz, withAnnotation(InjectProperty.class));

        allFields.forEach(field -> {
            InjectProperty annotation = field.getAnnotation(InjectProperty.class);
            String propertyValue = properties.getProperty(annotation.value());
            if (propertyValue != null) {
                field.setAccessible(true);
                try {
                    Object valueToInject = convertValue(field.getType(), propertyValue);
                    field.set(object, valueToInject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Object convertValue(Class<?> targetType, String value) {
        if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        // Поддержка других типов по мере необходимости
        return value;
    }
}
