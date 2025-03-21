package com.txokodev.app.domain;

import static com.txokodev.app.domain.AppUserTestSamples.*;
import static com.txokodev.app.domain.ProjectIdeaTestSamples.*;
import static com.txokodev.app.domain.ProposalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.txokodev.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProjectIdeaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectIdea.class);
        ProjectIdea projectIdea1 = getProjectIdeaSample1();
        ProjectIdea projectIdea2 = new ProjectIdea();
        assertThat(projectIdea1).isNotEqualTo(projectIdea2);

        projectIdea2.setId(projectIdea1.getId());
        assertThat(projectIdea1).isEqualTo(projectIdea2);

        projectIdea2 = getProjectIdeaSample2();
        assertThat(projectIdea1).isNotEqualTo(projectIdea2);
    }

    @Test
    void appUserTest() {
        ProjectIdea projectIdea = getProjectIdeaRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        projectIdea.setAppUser(appUserBack);
        assertThat(projectIdea.getAppUser()).isEqualTo(appUserBack);

        projectIdea.appUser(null);
        assertThat(projectIdea.getAppUser()).isNull();
    }

    @Test
    void proposalTest() {
        ProjectIdea projectIdea = getProjectIdeaRandomSampleGenerator();
        Proposal proposalBack = getProposalRandomSampleGenerator();

        projectIdea.addProposal(proposalBack);
        assertThat(projectIdea.getProposals()).containsOnly(proposalBack);
        assertThat(proposalBack.getProjectIdea()).isEqualTo(projectIdea);

        projectIdea.removeProposal(proposalBack);
        assertThat(projectIdea.getProposals()).doesNotContain(proposalBack);
        assertThat(proposalBack.getProjectIdea()).isNull();

        projectIdea.proposals(new HashSet<>(Set.of(proposalBack)));
        assertThat(projectIdea.getProposals()).containsOnly(proposalBack);
        assertThat(proposalBack.getProjectIdea()).isEqualTo(projectIdea);

        projectIdea.setProposals(new HashSet<>());
        assertThat(projectIdea.getProposals()).doesNotContain(proposalBack);
        assertThat(proposalBack.getProjectIdea()).isNull();
    }
}
