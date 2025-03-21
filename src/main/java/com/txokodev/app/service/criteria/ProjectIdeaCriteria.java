package com.txokodev.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.txokodev.app.domain.ProjectIdea} entity. This class is used
 * in {@link com.txokodev.app.web.rest.ProjectIdeaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /project-ideas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectIdeaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private IntegerFilter budget;

    private LocalDateFilter deadline;

    private StringFilter location;

    private LongFilter appUserId;

    private LongFilter proposalId;

    private Boolean distinct;

    public ProjectIdeaCriteria() {}

    public ProjectIdeaCriteria(ProjectIdeaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.budget = other.optionalBudget().map(IntegerFilter::copy).orElse(null);
        this.deadline = other.optionalDeadline().map(LocalDateFilter::copy).orElse(null);
        this.location = other.optionalLocation().map(StringFilter::copy).orElse(null);
        this.appUserId = other.optionalAppUserId().map(LongFilter::copy).orElse(null);
        this.proposalId = other.optionalProposalId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProjectIdeaCriteria copy() {
        return new ProjectIdeaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public IntegerFilter getBudget() {
        return budget;
    }

    public Optional<IntegerFilter> optionalBudget() {
        return Optional.ofNullable(budget);
    }

    public IntegerFilter budget() {
        if (budget == null) {
            setBudget(new IntegerFilter());
        }
        return budget;
    }

    public void setBudget(IntegerFilter budget) {
        this.budget = budget;
    }

    public LocalDateFilter getDeadline() {
        return deadline;
    }

    public Optional<LocalDateFilter> optionalDeadline() {
        return Optional.ofNullable(deadline);
    }

    public LocalDateFilter deadline() {
        if (deadline == null) {
            setDeadline(new LocalDateFilter());
        }
        return deadline;
    }

    public void setDeadline(LocalDateFilter deadline) {
        this.deadline = deadline;
    }

    public StringFilter getLocation() {
        return location;
    }

    public Optional<StringFilter> optionalLocation() {
        return Optional.ofNullable(location);
    }

    public StringFilter location() {
        if (location == null) {
            setLocation(new StringFilter());
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public LongFilter getAppUserId() {
        return appUserId;
    }

    public Optional<LongFilter> optionalAppUserId() {
        return Optional.ofNullable(appUserId);
    }

    public LongFilter appUserId() {
        if (appUserId == null) {
            setAppUserId(new LongFilter());
        }
        return appUserId;
    }

    public void setAppUserId(LongFilter appUserId) {
        this.appUserId = appUserId;
    }

    public LongFilter getProposalId() {
        return proposalId;
    }

    public Optional<LongFilter> optionalProposalId() {
        return Optional.ofNullable(proposalId);
    }

    public LongFilter proposalId() {
        if (proposalId == null) {
            setProposalId(new LongFilter());
        }
        return proposalId;
    }

    public void setProposalId(LongFilter proposalId) {
        this.proposalId = proposalId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectIdeaCriteria that = (ProjectIdeaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(budget, that.budget) &&
            Objects.equals(deadline, that.deadline) &&
            Objects.equals(location, that.location) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(proposalId, that.proposalId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, budget, deadline, location, appUserId, proposalId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectIdeaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalBudget().map(f -> "budget=" + f + ", ").orElse("") +
            optionalDeadline().map(f -> "deadline=" + f + ", ").orElse("") +
            optionalLocation().map(f -> "location=" + f + ", ").orElse("") +
            optionalAppUserId().map(f -> "appUserId=" + f + ", ").orElse("") +
            optionalProposalId().map(f -> "proposalId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
