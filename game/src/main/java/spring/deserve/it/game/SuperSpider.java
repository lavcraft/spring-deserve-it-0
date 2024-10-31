package spring.deserve.it.game;

import org.springframework.stereotype.Component;
import org.supercompany.spyders.api.RPSEnum;
import org.supercompany.spyders.api.Spider;

@Component
@DefaultSpider
@PlayerQualifier("superHero")
public class SuperSpider extends AbstractSpider{

//    @PostConstruct
//    public void init(){
//        System.exit(666);
//    }

    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return RPSEnum.PAPER;
    }
}
