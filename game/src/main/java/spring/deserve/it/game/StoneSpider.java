package spring.deserve.it.game;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

@Component
@Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)
@PlayerQualifier("Kirill")
public class StoneSpider extends AbstractSpider {


    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return RPSEnum.ROCK;
    }

    @PreDestroy
    public void closeAll(){
        System.out.println("Все что важно я закрыл, а что не важно и хрен с ним");
    }
}
