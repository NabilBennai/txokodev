package com.txokodev.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProjectIdeaCriteriaTest {

    @Test
    void newProjectIdeaCriteriaHasAllFiltersNullTest() {
        var projectIdeaCriteria = new ProjectIdeaCriteria();
        assertThat(projectIdeaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void projectIdeaCriteriaFluentMethodsCreatesFiltersTest() {
        var projectIdeaCriteria = new ProjectIdeaCriteria();

        setAllFilters(projectIdeaCriteria);

        assertThat(projectIdeaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void projectIdeaCriteriaCopyCreatesNullFilterTest() {
        var projectIdeaCriteria = new ProjectIdeaCriteria();
        var copy = projectIdeaCriteria.copy();

        assertThat(projectIdeaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(projectIdeaCriteria)
        );
    }

    @Test
    void projectIdeaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var projectIdeaCriteria = new ProjectIdeaCriteria();
        setAllFilters(projectIdeaCriteria);

        var copy = projectIdeaCriteria.copy();

        assertThat(projectIdeaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(projectIdeaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var projectIdeaCriteria = new ProjectIdeaCriteria();

        assertThat(projectIdeaCriteria).hasToString("ProjectIdeaCriteria{}");
    }

    private static void setAllFilters(ProjectIdeaCriteria projectIdeaCriteria) {
        projectIdeaCriteria.id();
        projectIdeaCriteria.title();
        projectIdeaCriteria.budget();
        projectIdeaCriteria.deadline();
        projectIdeaCriteria.location();
        projectIdeaCriteria.appUserId();
        projectIdeaCriteria.proposalId();
        projectIdeaCriteria.distinct();
    }

    private static Condition<ProjectIdeaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getBudget()) &&
                condition.apply(criteria.getDeadline()) &&
                condition.apply(criteria.getLocation()) &&
                condition.apply(criteria.getAppUserId()) &&
                condition.apply(criteria.getProposalId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProjectIdeaCriteria> copyFiltersAre(ProjectIdeaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getBudget(), copy.getBudget()) &&
                condition.apply(criteria.getDeadline(), copy.getDeadline()) &&
                condition.apply(criteria.getLocation(), copy.getLocation()) &&
                condition.apply(criteria.getAppUserId(), copy.getAppUserId()) &&
                condition.apply(criteria.getProposalId(), copy.getProposalId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
