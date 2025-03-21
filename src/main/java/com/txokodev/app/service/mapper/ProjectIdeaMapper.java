package com.txokodev.app.service.mapper;

import com.txokodev.app.domain.AppUser;
import com.txokodev.app.domain.ProjectIdea;
import com.txokodev.app.service.dto.AppUserDTO;
import com.txokodev.app.service.dto.ProjectIdeaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProjectIdea} and its DTO {@link ProjectIdeaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectIdeaMapper extends EntityMapper<ProjectIdeaDTO, ProjectIdea> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "appUserFullName")
    ProjectIdeaDTO toDto(ProjectIdea s);

    @Named("appUserFullName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    AppUserDTO toDtoAppUserFullName(AppUser appUser);
}
