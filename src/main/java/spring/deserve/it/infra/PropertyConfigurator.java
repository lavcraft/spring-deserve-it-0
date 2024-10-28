package spring.deserve.it.infra;

import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import spring.deserve.it.game.InjectProperty;
import spring.deserve.it.game.ObjectConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;

@Component
public class PropertyConfigurator implements ObjectConfigurator, BeanPostProcessor {

   @Autowired
   private Environment environment;



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        configure(bean);
        return bean;
    }

    @Override
    public void configure(Object object) {
        Class<?> clazz = object.getClass();

        // Рекурсивно получаем все поля, включая из суперклассов, с аннотацией @InjectProperty
        Set<Field> allFields = ReflectionUtils.getAllFields(clazz, withAnnotation(InjectProperty.class));

        allFields.forEach(field -> {
            InjectProperty annotation = field.getAnnotation(InjectProperty.class);
            String propertyValue = environment.getProperty(annotation.value());
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
