package spring.deserve.it.infra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SpyderCountEnvironmentPostProcessorTest {
    @InjectMocks                               SpyderCountEnvironmentPostProcessor epp;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) ConfigurableEnvironment             env;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) SpringApplication                   app;

    @Test
    void should_wort() {
        when(app.getMainApplicationClass()).thenAnswer(invocation -> SpyderCountEnvironmentPostProcessorTest.class);
        epp.postProcessEnvironment(env, app);
    }
}