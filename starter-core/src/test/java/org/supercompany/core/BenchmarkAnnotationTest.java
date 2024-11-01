package org.supercompany.core;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(Proxy.isProxyClass(bean.getClass()))
                .as("Should return java.lang proxy object. But %s", bean.getClass())
                .isTrue();

        assertThat(bean.getClass().getInterfaces())
                .as("Class should have interface")
                .contains(TestClassInterface.class);
    }
}