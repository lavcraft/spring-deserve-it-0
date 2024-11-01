package org.supercompany.spyders.scope;


import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.assertj.ApplicationContextAssert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.supercompany.spyders.api.PlayerQualifier;
import org.supercompany.spyders.api.RPSEnum;
import org.supercompany.spyders.api.Spider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                FightScopeConfiguration.class,
                FightScopeContextTest.Configuration.class,
        }
)
public class FightScopeContextTest {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public static abstract class ASpider implements Spider {
        @Getter
        @Setter
        private int lives = 5;

        @Override
        public RPSEnum fight(Spider opponent, int battleId) {
            return null;
        }

        @Override
        public boolean isAlive() {
            return lives > 0;
        }

        @Override
        public int getLives() {
            return 0;
        }

        @Override
        public void loseLife() {

        }

        @Override
        public void setLives(int lives) {

        }

        @Override
        public void setOwner(String owner) {

        }

        @Override
        public String getOwner() {
            return "";
        }
    }

    public static class PSpider extends ASpider {
    }

    public static class SSpider extends ASpider {
    }

    @TestConfiguration
    public static class Configuration {
        @SpiderBean
        @PlayerQualifier("ignored") //так не переопределяет игрока. Берётся из класса
        public PSpider paperSpider2() {
            return new PSpider();
        }

        @SpiderBean
        @PlayerQualifier("ignored") //так не переопределяет игрока. Берётся из класса
        public SSpider stoneSpider() {
            return new SSpider();
        }
    }

    @Test
    void should_create_new_spider_when_new_fight_has_started(ApplicationContext context) {
        //given
        var bean1  = context.getBean(SSpider.class);
        var bean11 = context.getBean(SSpider.class);
        var bean2  = context.getBean(PSpider.class);
        var bean22 = context.getBean(PSpider.class);

        //expect
        assertThat(bean1)
                .as("Return identical spider while until fight end")
                .isEqualTo(bean11);
        assertThat(bean2)
                .as("Return identical spider while until fight end")
                .isEqualTo(bean22);

        //when
        applicationEventPublisher.publishEvent(new FightFinishedEvent(List.of(bean1)));

        //then
        var bean111 = context.getBean("winner0");
        assertThat(bean111)
                .as("Return winner0 as first SSpider")
                .isEqualTo(bean1);
    }

    @Test
    void should_return_identical_spiders_until_fight_end(ApplicationContext context) {
        var bean1 = context.getBean(PSpider.class);
        var bean2 = context.getBean(PSpider.class);

        assertThat(bean1)
                .as("Beans in one scope should be identical")
                .isEqualTo(bean2);
    }

    //TODO пофиксите этот тест. Он падает только когда запускается в пачке с другими а не по отдельности
    @Test
    void should_refresh_scope_at_the_end_of_the_fight(
            ApplicationContext context
    ) {
        //given
        var bean1 = context.getBean(PSpider.class);
        var bean2 = context.getBean(SSpider.class);
        bean2.setLives(10);
        bean1.setLives(0);

        //when
        applicationEventPublisher.publishEvent(new FightFinishedEvent(List.of(bean1)));

        //then
        var bean22 = context.getBean(SSpider.class); //not so good, no human readable message when fail
        var bean3  = context.getBean("winner0"); //not so good, no human readable message when fail
        assertThat(bean3)
                .as("Should return winner, and winner is a bean1")
                .isSameAs(bean1);
        assertThat(bean22)
                .as("Should not delete from context because is alive")
                .isEqualTo(bean2);
        //and then
        assertThatThrownBy(
                () -> {
                    context.getBean(PSpider.class);
                }, "Should not get paper spider bean because he's dead"
        )
                .isInstanceOf(NoSuchBeanDefinitionException.class);

    }
}
