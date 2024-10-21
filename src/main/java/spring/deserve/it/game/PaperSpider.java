package spring.deserve.it.game;

import spring.deserve.it.api.RPSEnum;

public class PaperSpider extends AbstractSpider {
    @Override
    public RPSEnum fight() {
        return RPSEnum.PAPER;
    }
}
