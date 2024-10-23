package spring.deserve.it.game;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting game");

        GameMaster gameMaster =ObjectFactory.getInstance().createObject(GameMaster.class);
        gameMaster.fight();
    }

}
