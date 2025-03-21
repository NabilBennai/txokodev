package com.txokodev.app.service.mapper;

import com.txokodev.app.domain.AppUser;
import com.txokodev.app.domain.ProjectIdea;
import com.txokodev.app.domain.Proposal;
import com.txokodev.app.service.dto.AppUserDTO;
import com.txokodev.app.service.dto.ProjectIdeaDTO;
import com.txokodev.app.service.dto.ProposalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proposal} and its DTO {@link ProposalDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProposalMapper extends EntityMapper<ProposalDTO, Proposal> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "appUserFullName")
    @Mapping(target = "projectIdea", source = "projectIdea", qualifiedByName = "projectIdeaTitle")
    ProposalDTO toDto(Proposal s);

    @Named("appUserFullName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    AppUserDTO toDtoAppUserFullName(AppUser appUser);

    @Named("projectIdeaTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ProjectIdeaDTO toDtoProjectIdeaTitle(ProjectIdea projectIdea);
}
