package spring.deserve.it.game;

import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

public class GameMaster {

    private Spider spider1 = ObjectFactory.getInstance().createObject(PaperSpider.class);
    private Spider spider2 = ObjectFactory.getInstance().createObject(StoneSpider.class);





    public void fight() {
        System.out.println("Начинаем бой между " + spider1.getClass().getSimpleName() + " и " + spider2.getClass().getSimpleName() + "!");

        while (spider1.isAlive() && spider2.isAlive()) {
            RPSEnum move1 = spider1.fight();
            RPSEnum move2 = spider2.fight();

            System.out.println(spider1.getClass().getSimpleName() + " ходит: " + move1);
            System.out.println(spider2.getClass().getSimpleName() + " ходит: " + move2);


            // Логика боя остаётся прежней
            if (move1 == RPSEnum.ROCK && move2 == RPSEnum.SCISSORS) {
                spider2.loseLife();
            } else if (move1 == RPSEnum.SCISSORS && move2 == RPSEnum.PAPER) {
                spider2.loseLife();
            } else if (move1 == RPSEnum.PAPER && move2 == RPSEnum.ROCK) {
                spider2.loseLife();
            } else {
                spider1.loseLife();
            }

            System.out.println("Жизни " + spider1.getClass().getSimpleName() + ": " + spider1.getLives());
            System.out.println("Жизни " + spider2.getClass().getSimpleName() + ": " + spider2.getLives());
        }

        // Определяем победителя
        String winner = spider1.isAlive() ? spider1.getClass().getSimpleName() : spider2.getClass().getSimpleName();
        System.out.println("Бой окончен! Победитель: " + winner);
    }
}
