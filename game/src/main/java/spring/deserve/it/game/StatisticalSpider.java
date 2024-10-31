package spring.deserve.it.game;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.supercompany.spyders.api.RPSEnum;
import org.supercompany.spyders.api.Spider;

@Component
@PlayerQualifier("Albina")
public class StatisticalSpider extends AbstractSpider {


    @Inject
    private HistoricalService historicalService;



    @PostConstruct
    public void init(){
        System.out.println(System.nanoTime()+" "+this.getLives());
    }


    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        // Получаем общую статистику оппонента по всем боям
        HistoricalServiceImpl.SpiderStatistics opponentStats = historicalService.getSpiderStatistics(opponent.hashCode());

        // Если статистика пуста, создаём объект статистики с нулями, избегая null
        opponentStats = opponentStats != null ? opponentStats : new HistoricalServiceImpl.SpiderStatistics();

        // Определяем на основе общей статистики, какой ход оппонент, вероятно, сделает
        int rockCount = opponentStats.getRockCount();
        int paperCount = opponentStats.getPaperCount();
        int scissorsCount = opponentStats.getScissorsCount();

        // Простейший алгоритм: выбираем ход, который побеждает наиболее частый ход оппонента
        if (rockCount > paperCount && rockCount > scissorsCount) {
            return RPSEnum.PAPER;  // Побеждает камень
        } else if (paperCount > rockCount && paperCount > scissorsCount) {
            return RPSEnum.SCISSORS;  // Побеждает бумага
        } else {
            return RPSEnum.ROCK;  // Побеждает ножницы
        }
    }
}
