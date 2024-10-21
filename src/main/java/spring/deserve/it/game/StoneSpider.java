package spring.deserve.it.game;

import spring.deserve.it.api.RPSEnum;

public class StoneSpider extends AbstractSpider {
    @Override
    public RPSEnum fight() {
        return RPSEnum.ROCK;
    }
}
