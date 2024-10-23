package spring.deserve.it.game;

import org.junit.jupiter.api.Test;
import spring.deserve.it.infra.Singleton;

import static org.junit.jupiter.api.Assertions.*;

@Singleton
class ApplicationContextTest {

    static class MockNotSingleton{}


    @Test
    void testThatSingletonIsReallySingleton() {
        ApplicationContext context = new ApplicationContext("spring.deserve.it");
        ApplicationContextTest bean1 = context.getBean(ApplicationContextTest.class);
        ApplicationContextTest bean2 = context.getBean(ApplicationContextTest.class);

        assertEquals(bean1, bean2);

        MockNotSingleton prototype1 = context.getBean(MockNotSingleton.class);
        MockNotSingleton prototype2 = context.getBean(MockNotSingleton.class);

        assertNotEquals(prototype1,prototype2);
    }
}