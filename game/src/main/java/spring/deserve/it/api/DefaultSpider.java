package spring.deserve.it.api;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Conditional(DefaultSpiderCondition.class)
public @interface DefaultSpider{
}
