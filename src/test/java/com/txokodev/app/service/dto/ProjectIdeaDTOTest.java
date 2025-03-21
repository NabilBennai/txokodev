package com.txokodev.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.txokodev.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectIdeaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectIdeaDTO.class);
        ProjectIdeaDTO projectIdeaDTO1 = new ProjectIdeaDTO();
        projectIdeaDTO1.setId(1L);
        ProjectIdeaDTO projectIdeaDTO2 = new ProjectIdeaDTO();
        assertThat(projectIdeaDTO1).isNotEqualTo(projectIdeaDTO2);
        projectIdeaDTO2.setId(projectIdeaDTO1.getId());
        assertThat(projectIdeaDTO1).isEqualTo(projectIdeaDTO2);
        projectIdeaDTO2.setId(2L);
        assertThat(projectIdeaDTO1).isNotEqualTo(projectIdeaDTO2);
        projectIdeaDTO1.setId(null);
        assertThat(projectIdeaDTO1).isNotEqualTo(projectIdeaDTO2);
    }
}
