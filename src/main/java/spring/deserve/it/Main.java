package spring.deserve.it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import spring.deserve.it.game.GameMaster;


@SpringBootApplication
@EnableAspectJAutoProxy
public class Main {



    public static void main(String[] args) {
        System.out.println("Starting game");


        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        context.getBean(GameMaster.class).fight();

    }

}
