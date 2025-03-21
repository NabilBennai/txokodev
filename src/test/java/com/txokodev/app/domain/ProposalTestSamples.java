package com.txokodev.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProposalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Proposal getProposalSample1() {
        return new Proposal().id(1L).price(1);
    }

    public static Proposal getProposalSample2() {
        return new Proposal().id(2L).price(2);
    }

    public static Proposal getProposalRandomSampleGenerator() {
        return new Proposal().id(longCount.incrementAndGet()).price(intCount.incrementAndGet());
    }
}
