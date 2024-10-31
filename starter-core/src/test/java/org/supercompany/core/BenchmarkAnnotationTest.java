package org.supercompany.core;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        public TestClass testBeantestBeanTestBean() {
            return new TestClass();
        }
    }

    public interface TestClassInterface {
        void targetMethod();
    }

    public static class TestClass implements TestClassInterface {
        @Benchmark
        @Override
        public void targetMethod() {

        }
    }

    @Test
    void should_log_execution_time_on_benchmark_annotation(CapturedOutput capturedOutput) {
        var bean = applicationContext.getBean(TestClassInterface.class);

        bean.targetMethod();

        assertThat(capturedOutput.toString()).contains("invocation targetMethod time:");

    }
}