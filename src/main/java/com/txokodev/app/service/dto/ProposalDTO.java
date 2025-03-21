package com.txokodev.app.service.dto;

import com.txokodev.app.domain.enumeration.ProposalStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.txokodev.app.domain.Proposal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProposalDTO implements Serializable {

    private Long id;

    @Lob
    private String message;

    @NotNull
    private Integer price;

    private ProposalStatus status;

    @NotNull
    private AppUserDTO appUser;

    @NotNull
    private ProjectIdeaDTO projectIdea;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public void setStatus(ProposalStatus status) {
        this.status = status;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public ProjectIdeaDTO getProjectIdea() {
        return projectIdea;
    }

    public void setProjectIdea(ProjectIdeaDTO projectIdea) {
        this.projectIdea = projectIdea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProposalDTO)) {
            return false;
        }

        ProposalDTO proposalDTO = (ProposalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proposalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProposalDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            ", appUser=" + getAppUser() +
            ", projectIdea=" + getProjectIdea() +
            "}";
    }
}
