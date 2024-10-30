package spring.deserve.it.game;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

@Component
@DefaultSpider
@PlayerQualifier("superHero")
public class SuperSpider extends AbstractSpider{

    @PostConstruct
    public void init(){
        System.exit(666);
    }

    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return RPSEnum.PAPER;
    }
}
