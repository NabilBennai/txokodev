package com.txokodev.app.domain;

import static com.txokodev.app.domain.AppUserTestSamples.*;
import static com.txokodev.app.domain.ProjectIdeaTestSamples.*;
import static com.txokodev.app.domain.ProposalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.txokodev.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProposalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proposal.class);
        Proposal proposal1 = getProposalSample1();
        Proposal proposal2 = new Proposal();
        assertThat(proposal1).isNotEqualTo(proposal2);

        proposal2.setId(proposal1.getId());
        assertThat(proposal1).isEqualTo(proposal2);

        proposal2 = getProposalSample2();
        assertThat(proposal1).isNotEqualTo(proposal2);
    }

    @Test
    void appUserTest() {
        Proposal proposal = getProposalRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        proposal.setAppUser(appUserBack);
        assertThat(proposal.getAppUser()).isEqualTo(appUserBack);

        proposal.appUser(null);
        assertThat(proposal.getAppUser()).isNull();
    }

    @Test
    void projectIdeaTest() {
        Proposal proposal = getProposalRandomSampleGenerator();
        ProjectIdea projectIdeaBack = getProjectIdeaRandomSampleGenerator();

        proposal.setProjectIdea(projectIdeaBack);
        assertThat(proposal.getProjectIdea()).isEqualTo(projectIdeaBack);

        proposal.projectIdea(null);
        assertThat(proposal.getProjectIdea()).isNull();
    }
}
