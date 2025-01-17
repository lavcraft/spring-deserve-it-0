package org.supercompany.core;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig({
        BenchmarkAnnotationTest.Config.class,
        SuperCompanyCoreAutoConfiguration.class
})
@RequiredArgsConstructor
@ExtendWith(OutputCaptureExtension.class)
class BenchmarkAnnotationTest {
    final ApplicationContext applicationContext;

    public static class Config {
        @Bean
        public TestClass testBeanTestBeanTestBean() {
            return new TestClass();
        }

        @Bean
        public TestClassBasdeOnAbstract testClassBasdeOnAbstract() {
            return new TestClassBasdeOnAbstract();
        }
    }

    public static abstract class TestClassAbstract implements TestClassInterface {
        @Benchmark
        @Override
        public void targetMethod() {

        }
    }

    public interface TestClassInterface {
        void targetMethod();
    }

    public static class TestClassBasdeOnAbstract extends TestClassAbstract {
    }

    public static class TestClass implements TestClassInterface {
        @Benchmark
        @Override
        public void targetMethod() {

        }
    }

    @Test
    void should_log_execution_time_on_benchmark_annotation(CapturedOutput capturedOutput) {
        //given
        var bean = (TestClassInterface) applicationContext.getBean("testBeanTestBeanTestBean");

        //when
        bean.targetMethod();

        //then
        assertThat(capturedOutput.toString())
                .as("Should print time after targetMethod call")
                .contains("invocation targetMethod time:");
    }

    @Test
    void should_cast_benchmarked_object_to_interface() {
        //given
        var bean = applicationContext.getBean("testClassBasdeOnAbstract");

        //expect
//        assertThatThrownBy(
//                () -> {
//                    TestClassInterface a = (TestClassInterface) applicationContext.getBean("testClassBasdeOnAbstract");
//                })
//                .hasMessageContaining(
//                        "class jdk.proxy2.$Proxy28 cannot be cast to class org.supercompany.core.BenchmarkAnnotationTest$TestClassInterface")
//                .isInstanceOf(ClassCastException.class);

        assertThat(Proxy.isProxyClass(bean.getClass()))
                .as("Should return java.lang proxy object. But %s", bean.getClass())
                .isTrue();

        assertThat(bean.getClass().getInterfaces())
                .as("Class should have interface")
                .contains(TestClassInterface.class);
    }

    //TODO тест для воспроизведение проблемы с последнего дня с ClassCast Exception
    // возникает из за кода вида bean.getClass().getInterfaces() т.к у нас есть суперкласс- абстрактный класс,
    // в базовом классе нет интерфейсов, нужно либо использовать ClassUtils либо руками забрать рекурсивно по всем родителям все интерфейсы
    @Test
    @Disabled
    void should_throw_cast_exception_when_no_interface_if_use_getInterfaces_on_abstract_class() {
        //given
        var bean = applicationContext.getBean("testClassBasdeOnAbstract");

        //expect
        assertThatThrownBy(
                () -> {
                    TestClassInterface a = (TestClassInterface) applicationContext.getBean("testClassBasdeOnAbstract");
                })
                .hasMessageContaining(
                        "class jdk.proxy2.$Proxy28 cannot be cast to class org.supercompany.core.BenchmarkAnnotationTest$TestClassInterface")
                .isInstanceOf(ClassCastException.class);

    }
}