package spring.deserve.it.game;

import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

public class ScissorsSpider extends AbstractSpider {
    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return  RPSEnum.SCISSORS;
    }
}
