package org.supercompany.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class BenchmarkBPP implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> originalClass = bean.getClass();

        var shouldProxy = Arrays.stream(originalClass.getDeclaredMethods())
                                .anyMatch(method -> method.isAnnotationPresent(Benchmark.class));

        if (shouldProxy) {
            return Proxy.newProxyInstance(
                    originalClass.getClassLoader(), originalClass.getInterfaces(), (proxy, method, args) -> {
                        var originalMethod = originalClass.getMethod(method.getName());
                        if (originalMethod.isAnnotationPresent(Benchmark.class)) {

                            var startTime = System.nanoTime();
                            var invoke    = method.invoke(bean, args);
                            var endTime   = System.nanoTime();
                            System.out.printf(
                                    "invocation %s time: %d ns%n",
                                    originalMethod.getName(),
                                    endTime - startTime
                            );
                            return invoke;
                        }


                        return method.invoke(bean, args);
                    }
            );
        }

        return bean;
    }
}

