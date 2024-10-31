package spring.deserve.it;

import jakarta.annotation.PostConstruct;
import org.supercompany.core.Singleton;

@Singleton
public class LegacyBean {

    public LegacyBean() {
        System.out.println("LEGACY BEAN WAS CREATED");
    }
}
