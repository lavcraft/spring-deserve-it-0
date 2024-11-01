package org.supercompany.spyders.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Scope(value = "fightScope")
public @interface FightScope {
    String value() default "fightScope";
}
