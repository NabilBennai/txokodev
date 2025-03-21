package com.txokodev.app.service.mapper;

import static com.txokodev.app.domain.ProjectIdeaAsserts.*;
import static com.txokodev.app.domain.ProjectIdeaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProjectIdeaMapperTest {

    private ProjectIdeaMapper projectIdeaMapper;

    @BeforeEach
    void setUp() {
        projectIdeaMapper = new ProjectIdeaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProjectIdeaSample1();
        var actual = projectIdeaMapper.toEntity(projectIdeaMapper.toDto(expected));
        assertProjectIdeaAllPropertiesEquals(expected, actual);
    }
}
