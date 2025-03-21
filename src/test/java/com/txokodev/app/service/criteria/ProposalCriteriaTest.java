package com.txokodev.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProposalCriteriaTest {

    @Test
    void newProposalCriteriaHasAllFiltersNullTest() {
        var proposalCriteria = new ProposalCriteria();
        assertThat(proposalCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void proposalCriteriaFluentMethodsCreatesFiltersTest() {
        var proposalCriteria = new ProposalCriteria();

        setAllFilters(proposalCriteria);

        assertThat(proposalCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void proposalCriteriaCopyCreatesNullFilterTest() {
        var proposalCriteria = new ProposalCriteria();
        var copy = proposalCriteria.copy();

        assertThat(proposalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(proposalCriteria)
        );
    }

    @Test
    void proposalCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var proposalCriteria = new ProposalCriteria();
        setAllFilters(proposalCriteria);

        var copy = proposalCriteria.copy();

        assertThat(proposalCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(proposalCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var proposalCriteria = new ProposalCriteria();

        assertThat(proposalCriteria).hasToString("ProposalCriteria{}");
    }

    private static void setAllFilters(ProposalCriteria proposalCriteria) {
        proposalCriteria.id();
        proposalCriteria.price();
        proposalCriteria.status();
        proposalCriteria.appUserId();
        proposalCriteria.projectIdeaId();
        proposalCriteria.distinct();
    }

    private static Condition<ProposalCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAppUserId()) &&
                condition.apply(criteria.getProjectIdeaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProposalCriteria> copyFiltersAre(ProposalCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAppUserId(), copy.getAppUserId()) &&
                condition.apply(criteria.getProjectIdeaId(), copy.getProjectIdeaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
