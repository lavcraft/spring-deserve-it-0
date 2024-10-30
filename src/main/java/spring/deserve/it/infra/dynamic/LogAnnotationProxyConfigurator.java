package spring.deserve.it.infra.dynamic;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import spring.deserve.it.game.Log;
import spring.deserve.it.infra.ProxyConfigurator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

//@Component
public class LogAnnotationProxyConfigurator implements BeanPostProcessor {

    @Autowired
    private ConfigurableListableBeanFactory factory;

    @Override
    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
        if (beanDefinition.getBeanClassName() != null) {
            Class<?> originalClass = Class.forName(beanDefinition.getBeanClassName());
            return wrapWithProxy(bean, originalClass);
        }

        return bean;
    }

    @SneakyThrows
    public <T> T wrapWithProxy(T obj, Class implClass) {
        // Проверяем, есть ли методы с аннотацией @Log в реализации класса
        if (Arrays.stream(implClass.getMethods()).anyMatch(this::hasLogAnnotation)) {
            // Создаем прокси-объект
            return (T) Proxy.newProxyInstance(implClass.getClassLoader(), implClass.getInterfaces(), new InvocationHandler() {
                @Override
                @SneakyThrows
                public Object invoke(Object proxy, Method method, Object[] args) {
                    // Получаем оригинальный метод из реализации класса
                    Method originalMethod = implClass.getMethod(method.getName(), method.getParameterTypes());

                    // Проверяем, есть ли у оригинального метода аннотация @Log
                    if (originalMethod.isAnnotationPresent(Log.class)) {
                        Log logAnnotation = originalMethod.getAnnotation(Log.class);
                        logFieldsBefore(obj, logAnnotation.value());  // Логируем перед вызовом метода

                        Object result = originalMethod.invoke(obj, args);  // Вызов реального метода

                        logFieldsAfter(obj, logAnnotation.value());  // Логируем после вызова метода
                        return result;
                    }
                    return originalMethod.invoke(obj, args);  // Если аннотации нет, просто вызываем метод
                }
            });
        }
        return obj;
    }

    // Логирование состояния полей перед вызовом метода
    @SneakyThrows
    private void logFieldsBefore(Object obj, String[] fields) {
        System.out.println("Логируем перед вызовом метода:");
        for (String field : fields) {
            Method getter = obj.getClass().getMethod("get" + capitalize(field));
            Object value = getter.invoke(obj);
            System.out.println(field + " = " + value);
        }
    }

    // Логирование состояния полей после вызова метода
    @SneakyThrows
    private void logFieldsAfter(Object obj, String[] fields) {
        System.out.println("Логируем после вызова метода:");
        for (String field : fields) {
            Method getter = obj.getClass().getMethod("get" + capitalize(field));
            Object value = getter.invoke(obj);
            System.out.println(field + " = " + value);
        }
    }

    private boolean hasLogAnnotation(Method method) {
        return method.isAnnotationPresent(Log.class);
    }

    private String capitalize(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
