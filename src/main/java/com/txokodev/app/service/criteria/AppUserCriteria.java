package com.txokodev.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.txokodev.app.domain.AppUser} entity. This class is used
 * in {@link com.txokodev.app.web.rest.AppUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter bio;

    private StringFilter city;

    private BooleanFilter isDeveloper;

    private LongFilter userId;

    private LongFilter projectIdeaId;

    private LongFilter proposalId;

    private Boolean distinct;

    public AppUserCriteria() {}

    public AppUserCriteria(AppUserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.bio = other.optionalBio().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.isDeveloper = other.optionalIsDeveloper().map(BooleanFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.projectIdeaId = other.optionalProjectIdeaId().map(LongFilter::copy).orElse(null);
        this.proposalId = other.optionalProposalId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppUserCriteria copy() {
        return new AppUserCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getBio() {
        return bio;
    }

    public Optional<StringFilter> optionalBio() {
        return Optional.ofNullable(bio);
    }

    public StringFilter bio() {
        if (bio == null) {
            setBio(new StringFilter());
        }
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
    }

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public BooleanFilter getIsDeveloper() {
        return isDeveloper;
    }

    public Optional<BooleanFilter> optionalIsDeveloper() {
        return Optional.ofNullable(isDeveloper);
    }

    public BooleanFilter isDeveloper() {
        if (isDeveloper == null) {
            setIsDeveloper(new BooleanFilter());
        }
        return isDeveloper;
    }

    public void setIsDeveloper(BooleanFilter isDeveloper) {
        this.isDeveloper = isDeveloper;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getProjectIdeaId() {
        return projectIdeaId;
    }

    public Optional<LongFilter> optionalProjectIdeaId() {
        return Optional.ofNullable(projectIdeaId);
    }

    public LongFilter projectIdeaId() {
        if (projectIdeaId == null) {
            setProjectIdeaId(new LongFilter());
        }
        return projectIdeaId;
    }

    public void setProjectIdeaId(LongFilter projectIdeaId) {
        this.projectIdeaId = projectIdeaId;
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
        final AppUserCriteria that = (AppUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(city, that.city) &&
            Objects.equals(isDeveloper, that.isDeveloper) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(projectIdeaId, that.projectIdeaId) &&
            Objects.equals(proposalId, that.proposalId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, bio, city, isDeveloper, userId, projectIdeaId, proposalId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalBio().map(f -> "bio=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalIsDeveloper().map(f -> "isDeveloper=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalProjectIdeaId().map(f -> "projectIdeaId=" + f + ", ").orElse("") +
            optionalProposalId().map(f -> "proposalId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
