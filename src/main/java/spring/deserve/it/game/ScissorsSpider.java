package spring.deserve.it.game;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

@Component
@Scope(scopeName = "prototype")
public class ScissorsSpider extends AbstractSpider {
    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        return  RPSEnum.SCISSORS;
    }
}
