package com.txokodev.app.service.criteria;

import com.txokodev.app.domain.enumeration.ProposalStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.txokodev.app.domain.Proposal} entity. This class is used
 * in {@link com.txokodev.app.web.rest.ProposalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /proposals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProposalCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProposalStatus
     */
    public static class ProposalStatusFilter extends Filter<ProposalStatus> {

        public ProposalStatusFilter() {}

        public ProposalStatusFilter(ProposalStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProposalStatusFilter copy() {
            return new ProposalStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter price;

    private ProposalStatusFilter status;

    private LongFilter appUserId;

    private LongFilter projectIdeaId;

    private Boolean distinct;

    public ProposalCriteria() {}

    public ProposalCriteria(ProposalCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ProposalStatusFilter::copy).orElse(null);
        this.appUserId = other.optionalAppUserId().map(LongFilter::copy).orElse(null);
        this.projectIdeaId = other.optionalProjectIdeaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProposalCriteria copy() {
        return new ProposalCriteria(this);
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

    public IntegerFilter getPrice() {
        return price;
    }

    public Optional<IntegerFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public IntegerFilter price() {
        if (price == null) {
            setPrice(new IntegerFilter());
        }
        return price;
    }

    public void setPrice(IntegerFilter price) {
        this.price = price;
    }

    public ProposalStatusFilter getStatus() {
        return status;
    }

    public Optional<ProposalStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ProposalStatusFilter status() {
        if (status == null) {
            setStatus(new ProposalStatusFilter());
        }
        return status;
    }

    public void setStatus(ProposalStatusFilter status) {
        this.status = status;
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
        final ProposalCriteria that = (ProposalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(status, that.status) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(projectIdeaId, that.projectIdeaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, status, appUserId, projectIdeaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalAppUserId().map(f -> "appUserId=" + f + ", ").orElse("") +
            optionalProjectIdeaId().map(f -> "projectIdeaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
