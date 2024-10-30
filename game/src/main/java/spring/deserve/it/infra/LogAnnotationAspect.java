package spring.deserve.it.infra;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import spring.deserve.it.game.Log;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LogAnnotationAspect {

    @Around("@annotation(logAnnotation)")
    public Object logAround(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        Object target = joinPoint.getTarget();

        // Логируем состояние полей перед вызовом метода
        logFieldsBefore(target, logAnnotation.value());

        // Выполняем метод
        Object result = joinPoint.proceed();

        // Логируем состояние полей после вызова метода
        logFieldsAfter(target, logAnnotation.value());

        return result;
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

    private String capitalize(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}