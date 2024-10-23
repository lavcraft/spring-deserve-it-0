package spring.deserve.it.game;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting game");


        new ApplicationContext("spring.deserve.it").getBean(GameMaster.class).fight();

    }

}
