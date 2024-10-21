package spring.deserve.it.infra;

import org.junit.jupiter.api.Test;
import spring.deserve.it.game.StoneSpider;

import static org.junit.jupiter.api.Assertions.*;
class PropertyConfiguratorTest {

    @Test
    public void testInjectProperty() {
        // Создаем объект паука
        StoneSpider spider = new StoneSpider();

        // Загружаем проперти и инжектируем их в паука
        PropertyConfigurator loader = new PropertyConfigurator();
        loader.configure(spider);

        // Проверяем, что количество жизней было корректно заинжектировано
        assertEquals(5, spider.getLives());  // Допустим, в application.properties стоит "spider.default.lives=5"
    }
}