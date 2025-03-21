package com.txokodev.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.txokodev.app.domain.AppUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserDTO implements Serializable {

    private Long id;

    @NotNull
    private String fullName;

    private String bio;

    @NotNull
    private String city;

    @NotNull
    private Boolean isDeveloper;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getIsDeveloper() {
        return isDeveloper;
    }

    public void setIsDeveloper(Boolean isDeveloper) {
        this.isDeveloper = isDeveloper;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDTO)) {
            return false;
        }

        AppUserDTO appUserDTO = (AppUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", bio='" + getBio() + "'" +
            ", city='" + getCity() + "'" +
            ", isDeveloper='" + getIsDeveloper() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
