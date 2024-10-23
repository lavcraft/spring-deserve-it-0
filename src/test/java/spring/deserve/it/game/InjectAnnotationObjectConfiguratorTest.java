package spring.deserve.it.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectAnnotationObjectConfiguratorTest {

    @Inject
    private StoneSpider spider;


    private PaperSpider paperSpider;


    @Inject
    public void setPaperSpider(PaperSpider paperSpider) {
        this.paperSpider = paperSpider;
    }

    @Test
    void injectForFieldsIsWorking() {
        InjectAnnotationObjectConfigurator configurator = new InjectAnnotationObjectConfigurator();
        configurator.configure(this);

        assertNotNull(spider);

    }

    @Test
    void testInjectIsWorkingForMethods() {
        InjectAnnotationObjectConfigurator configurator = new InjectAnnotationObjectConfigurator();
        configurator.configure(this);
        assertNotNull(paperSpider);
    }
}