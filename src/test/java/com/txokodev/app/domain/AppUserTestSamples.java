package com.txokodev.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser().id(1L).fullName("fullName1").bio("bio1").city("city1");
    }

    public static AppUser getAppUserSample2() {
        return new AppUser().id(2L).fullName("fullName2").bio("bio2").city("city2");
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .bio(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString());
    }
}
