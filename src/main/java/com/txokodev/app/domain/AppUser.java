package com.txokodev.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "bio")
    private String bio;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "is_developer", nullable = false)
    private Boolean isDeveloper;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "proposals" }, allowSetters = true)
    private Set<ProjectIdea> projectIdeas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "projectIdea" }, allowSetters = true)
    private Set<Proposal> proposals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public AppUser fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return this.bio;
    }

    public AppUser bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return this.city;
    }

    public AppUser city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getIsDeveloper() {
        return this.isDeveloper;
    }

    public AppUser isDeveloper(Boolean isDeveloper) {
        this.setIsDeveloper(isDeveloper);
        return this;
    }

    public void setIsDeveloper(Boolean isDeveloper) {
        this.isDeveloper = isDeveloper;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<ProjectIdea> getProjectIdeas() {
        return this.projectIdeas;
    }

    public void setProjectIdeas(Set<ProjectIdea> projectIdeas) {
        if (this.projectIdeas != null) {
            this.projectIdeas.forEach(i -> i.setAppUser(null));
        }
        if (projectIdeas != null) {
            projectIdeas.forEach(i -> i.setAppUser(this));
        }
        this.projectIdeas = projectIdeas;
    }

    public AppUser projectIdeas(Set<ProjectIdea> projectIdeas) {
        this.setProjectIdeas(projectIdeas);
        return this;
    }

    public AppUser addProjectIdea(ProjectIdea projectIdea) {
        this.projectIdeas.add(projectIdea);
        projectIdea.setAppUser(this);
        return this;
    }

    public AppUser removeProjectIdea(ProjectIdea projectIdea) {
        this.projectIdeas.remove(projectIdea);
        projectIdea.setAppUser(null);
        return this;
    }

    public Set<Proposal> getProposals() {
        return this.proposals;
    }

    public void setProposals(Set<Proposal> proposals) {
        if (this.proposals != null) {
            this.proposals.forEach(i -> i.setAppUser(null));
        }
        if (proposals != null) {
            proposals.forEach(i -> i.setAppUser(this));
        }
        this.proposals = proposals;
    }

    public AppUser proposals(Set<Proposal> proposals) {
        this.setProposals(proposals);
        return this;
    }

    public AppUser addProposal(Proposal proposal) {
        this.proposals.add(proposal);
        proposal.setAppUser(this);
        return this;
    }

    public AppUser removeProposal(Proposal proposal) {
        this.proposals.remove(proposal);
        proposal.setAppUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return getId() != null && getId().equals(((AppUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", bio='" + getBio() + "'" +
            ", city='" + getCity() + "'" +
            ", isDeveloper='" + getIsDeveloper() + "'" +
            "}";
    }
}
