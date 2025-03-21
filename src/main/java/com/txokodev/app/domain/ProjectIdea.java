package com.txokodev.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProjectIdea.
 */
@Entity
@Table(name = "project_idea")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectIdea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "budget", nullable = false)
    private Integer budget;

    @Column(name = "deadline")
    private LocalDate deadline;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "projectIdeas", "proposals" }, allowSetters = true)
    private AppUser appUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "projectIdea")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "projectIdea" }, allowSetters = true)
    private Set<Proposal> proposals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProjectIdea id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public ProjectIdea title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public ProjectIdea description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBudget() {
        return this.budget;
    }

    public ProjectIdea budget(Integer budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public ProjectIdea deadline(LocalDate deadline) {
        this.setDeadline(deadline);
        return this;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getLocation() {
        return this.location;
    }

    public ProjectIdea location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public ProjectIdea appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Set<Proposal> getProposals() {
        return this.proposals;
    }

    public void setProposals(Set<Proposal> proposals) {
        if (this.proposals != null) {
            this.proposals.forEach(i -> i.setProjectIdea(null));
        }
        if (proposals != null) {
            proposals.forEach(i -> i.setProjectIdea(this));
        }
        this.proposals = proposals;
    }

    public ProjectIdea proposals(Set<Proposal> proposals) {
        this.setProposals(proposals);
        return this;
    }

    public ProjectIdea addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setProjectIdea(this);
        return this;
    }

    public ProjectIdea removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setProjectIdea(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectIdea)) {
            return false;
        }
        return getId() != null && getId().equals(((ProjectIdea) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectIdea{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", budget=" + getBudget() +
            ", deadline='" + getDeadline() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
