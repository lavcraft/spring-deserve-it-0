package spring.deserve.it.game;

import org.springframework.stereotype.Component;
import org.supercompany.spyders.api.RPSEnum;
import org.supercompany.spyders.api.Spider;

@Component
@PlayerQualifier("Jack")
@DefaultSpider
public class PaperSpider extends AbstractSpider {

    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return RPSEnum.PAPER;
    }
}
