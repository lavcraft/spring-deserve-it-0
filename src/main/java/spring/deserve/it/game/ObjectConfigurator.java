package spring.deserve.it.game;

public interface ObjectConfigurator {
    void configure(Object obj);

    default void setApplicationContext(ApplicationContext context) {
    }


}
