package spring.deserve.it.infra.dynamic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import spring.deserve.it.game.Log;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Тест в отдельном пакете для того чтобы создать classpath с котекстом для выполнения когда есть только
 * proxyConfigurator основанный на DynamicProxy. Костыль из-за нашего механизма детектирования конфигураторов с помощью
 * Reflections и отсутствия механизмов настройки
 */
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                ApplicationContextDynamicProxyLogTest.class,
                ApplicationContextDynamicProxyLogTest.Configuration.class
        }
)
@Component("spring.deserve.it.infra.dynamic.ApplicationContextDynamicProxyLogTest.ORIGINAL")
@RequiredArgsConstructor
public class ApplicationContextDynamicProxyLogTest {
    private final ApplicationContext context;

    @TestConfiguration
    public static class Configuration {

// Через объявление в @Bean не будет работать, подумайте почему
        @Bean
        public TestBean bean() {
            return new TestBean();
        }

        @Bean
        public static LogAnnotationProxyConfigurator logAnnotationProxyConfigurator() {
            return new LogAnnotationProxyConfigurator();
        }
        @Bean
        public static LogAnnotationProxyConfigurator logAnnotationProxyConfigurator2() {
            return new LogAnnotationProxyConfigurator();
        }
        @Bean
        public static LogAnnotationProxyConfigurator logAnnotationProxyConfigurator3() {
            return new LogAnnotationProxyConfigurator();
        }

        //        @Component
        @Getter
        public static class TestBean implements ApplicationContextDynamicProxyLogTestInterface {
            private String someProp = "someProp";

            @Log("someProp")
            public void tryLog() {
            }
        }
    }


    @Test
    void should_get_bean_by_interface() {
        //given
        ApplicationContextDynamicProxyLogTestInterface bean = context.getBean(
                ApplicationContextDynamicProxyLogTestInterface.class);

        //expect
        assertThat(bean)
                .as("Should get bean by interface")
                .isNotNull();
    }

    @Test
    @Disabled("Now work, because migrate to spring")
    void should_not_work_without_interface() {
        //expect
        Assertions.assertThatThrownBy(() -> {
            ApplicationContextDynamicProxyLogTest bean = context.getBean(ApplicationContextDynamicProxyLogTest.class);
        });
    }

    @Test
    void should_log_some_field_by_interface(CapturedOutput capturedOutput) {
        //given
        ApplicationContextDynamicProxyLogTestInterface bean = context.getBean(
                ApplicationContextDynamicProxyLogTestInterface.class);

        //when
        bean.tryLog();

        assertThat(capturedOutput.toString())
                .as("Should log valid data")
                .contains("someProp = someProp");
    }

}
