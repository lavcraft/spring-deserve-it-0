package spring.deserve.it.game;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.supercompany.core.Benchmark;
import org.supercompany.spyders.api.RPSEnum;
import org.supercompany.spyders.api.Spider;

@Component
@Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)
@PlayerQualifier("Kirill")
public class StoneSpider extends AbstractSpider {


    @Override
    @Benchmark
    public RPSEnum fight(Spider opponent, int battleId) {
        return RPSEnum.ROCK;
    }

    @PreDestroy
    public void closeAll(){
        System.out.println("Все что важно я закрыл, а что не важно и хрен с ним");
    }
}
