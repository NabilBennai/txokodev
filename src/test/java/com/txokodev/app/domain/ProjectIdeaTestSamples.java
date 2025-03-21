package com.txokodev.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProjectIdeaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProjectIdea getProjectIdeaSample1() {
        return new ProjectIdea().id(1L).title("title1").budget(1).location("location1");
    }

    public static ProjectIdea getProjectIdeaSample2() {
        return new ProjectIdea().id(2L).title("title2").budget(2).location("location2");
    }

    public static ProjectIdea getProjectIdeaRandomSampleGenerator() {
        return new ProjectIdea()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .budget(intCount.incrementAndGet())
            .location(UUID.randomUUID().toString());
    }
}
