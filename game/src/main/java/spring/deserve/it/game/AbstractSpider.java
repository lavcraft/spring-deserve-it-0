package spring.deserve.it.game;

import lombok.Getter;
import lombok.Setter;
import org.supercompany.spyders.api.InjectProperty;
import org.supercompany.spyders.api.Spider;


@Getter
@Setter
public abstract class AbstractSpider implements Spider {
    private String owner = "unknown";
    @InjectProperty
            ("spider.default.lives")
    private int    lives;

    public boolean isAlive() {
        return lives > 0;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }
}

