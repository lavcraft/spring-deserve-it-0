package spring.deserve.it.infra.dynamic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import spring.deserve.it.game.Log;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Тест в отдельном пакете для того чтобы создать classpath с котекстом для выполнения когда есть только
 * proxyConfigurator основанный на DynamicProxy. Костыль из-за нашего механизма детектирования конфигураторов с помощью
 * Reflections и отсутствия механизмов настройки
 */
@ExtendWith(OutputCaptureExtension.class)
public class MultipleBPPWithProxyTest {
    ApplicationContextRunner runner = new ApplicationContextRunner();

    @TestConfiguration
    public static class Configuration {
        @Bean
        public LogAnnotationProxyConfigurator logAnnotationProxyConfigurator() {
            return new LogAnnotationProxyConfigurator();
        }
        @Bean
        public LogAnnotationProxyConfigurator logAnnotationProxyConfigurator2() {
            return new LogAnnotationProxyConfigurator();
        }
    }

    public static class TestBean implements ApplicationContextDynamicProxyLogTestInterface {
        private String someProp = "someProp";

        @Log("someProp")
        public void tryLog() {
        }
    }

    @Test
    void should_get_bean_by_interface() {
        //given
        runner.withBean(TestBean.class)
              .withUserConfiguration(Configuration.class)
              .run(context -> {
                  ApplicationContextDynamicProxyLogTestInterface bean = context.getBean(
                          ApplicationContextDynamicProxyLogTestInterface.class);

                  assertThat(bean)
                          .as("Should get bean by interface")
                          .isNotNull();
              });
    }

//    @Test
//    void should_not_work_without_interface() {
//        //expect
//        Assertions.assertThatThrownBy(() -> {
//            ApplicationContextDynamicProxyLogTest2 bean = context.getBean(ApplicationContextDynamicProxyLogTest2.class);
//        });
//    }
//
//    @Test
//    void should_log_some_field_by_interface(CapturedOutput capturedOutput) {
//        //given
//        ApplicationContextDynamicProxyLogTestInterface bean = context.getBean(
//                ApplicationContextDynamicProxyLogTestInterface.class);
//
//        //when
//        bean.tryLog();
//
//        assertThat(capturedOutput.toString())
//                .as("Should log valid data")
//                .contains("someProp = someProp");
//    }

}
