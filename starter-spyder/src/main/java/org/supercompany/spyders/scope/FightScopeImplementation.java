package org.supercompany.spyders.scope;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.supercompany.spyders.api.Spider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FightScopeImplementation implements Scope {
    private final Map<String, Spider> scopedObjects = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (!scopedObjects.containsKey(name)) {
            scopedObjects.put(name, (Spider) objectFactory.getObject());
        }

        return scopedObjects.get(name);
    }

    @Override
    public Object remove(String name) {
        return scopedObjects.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return "fightScope";
    }

    void reset() {
        for (var entry : this.scopedObjects.entrySet()) {
            if (!entry.getValue().isAlive()) {
                this.scopedObjects.remove(entry.getKey());
            }
        }
    }
}
