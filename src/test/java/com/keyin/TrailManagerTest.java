package com.keyin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TrailManagerTest {

    @Test
    public void testAddTrail() {
        Trail trail = new Trail();
        trail.setId(1L);
        trail.setName("test");
        trail.setLength(5);
        trail.setLoop(false);
        trail.setEndLocation("5678");
        trail.setStartLocation("1234");

        TrailManager trailManagerUnderTest = new TrailManager();

        Assertions.assertEquals(0, trailManagerUnderTest.getTrails().size());

        trailManagerUnderTest.addTrail(trail);

        Assertions.assertEquals(1, trailManagerUnderTest.getTrails().size());

        Trail trail2 = new Trail();
        trail2.setId(2L);
        trail2.setName("test");
        trail2.setLength(10);
        trail2.setLoop(false);
        trail2.setEndLocation("5678-0987");
        trail2.setStartLocation("1234-4321");

        trailManagerUnderTest.addTrail(trail2);

        // business rules are that no trails can have the same name.
        Assertions.assertEquals(1, trailManagerUnderTest.getTrails().size());
    }
}
