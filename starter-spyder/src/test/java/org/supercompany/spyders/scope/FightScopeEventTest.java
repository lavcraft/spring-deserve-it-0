package org.supercompany.spyders.scope;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.EventObject;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@RecordApplicationEvents
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FightScopeEventTest {
    @Autowired
    ApplicationEvents events;
    final ApplicationEventPublisher publisher;


    @Test
    void name() {
        publisher.publishEvent(new FightFinishedEvent(Collections.emptyList()));


        var collect = events.stream()
                            .map(EventObject::toString)
                            .collect(Collectors.joining("\n"));
        System.out.println("collect = " + collect);
    }
}
