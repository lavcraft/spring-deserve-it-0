package org.supercompany.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class BenchmarkBPP implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> originalClass = bean.getClass();

        //TODO всегда ли это будет работать? Что будет если до отработал другой BPP и сделал wrapproxy, какие есть варианты
        // Решить проблему на корню, придумать надёжный способ. PS: Смотри другие методы BPP
        var shouldProxy = Arrays.stream(ReflectionUtils.getAllDeclaredMethods(originalClass))
                                .anyMatch(method -> method.isAnnotationPresent(Benchmark.class));

        if (shouldProxy) {
//            Class<?>[] interfaces = originalClass.getInterfaces();
            Class<?>[] interfaces = ClassUtils.getAllInterfaces(bean);

            return Proxy.newProxyInstance(originalClass.getClassLoader(), interfaces, (proxy, method, args) -> {
                        var originalMethod = originalClass.getMethod(method.getName(), method.getParameterTypes());
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

