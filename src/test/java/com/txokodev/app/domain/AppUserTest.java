package com.txokodev.app.domain;

import static com.txokodev.app.domain.AppUserTestSamples.*;
import static com.txokodev.app.domain.ProjectIdeaTestSamples.*;
import static com.txokodev.app.domain.ProposalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.txokodev.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void projectIdeaTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        ProjectIdea projectIdeaBack = getProjectIdeaRandomSampleGenerator();

        appUser.addProjectIdea(projectIdeaBack);
        assertThat(appUser.getProjectIdeas()).containsOnly(projectIdeaBack);
        assertThat(projectIdeaBack.getAppUser()).isEqualTo(appUser);

        appUser.removeProjectIdea(projectIdeaBack);
        assertThat(appUser.getProjectIdeas()).doesNotContain(projectIdeaBack);
        assertThat(projectIdeaBack.getAppUser()).isNull();

        appUser.projectIdeas(new HashSet<>(Set.of(projectIdeaBack)));
        assertThat(appUser.getProjectIdeas()).containsOnly(projectIdeaBack);
        assertThat(projectIdeaBack.getAppUser()).isEqualTo(appUser);

        appUser.setProjectIdeas(new HashSet<>());
        assertThat(appUser.getProjectIdeas()).doesNotContain(projectIdeaBack);
        assertThat(projectIdeaBack.getAppUser()).isNull();
    }

    @Test
    void proposalTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Proposal proposalBack = getProposalRandomSampleGenerator();

        appUser.addProposal(proposalBack);
        assertThat(appUser.getProposals()).containsOnly(proposalBack);
        assertThat(proposalBack.getAppUser()).isEqualTo(appUser);

        appUser.removeProposal(proposalBack);
        assertThat(appUser.getProposals()).doesNotContain(proposalBack);
        assertThat(proposalBack.getAppUser()).isNull();

        appUser.proposals(new HashSet<>(Set.of(proposalBack)));
        assertThat(appUser.getProposals()).containsOnly(proposalBack);
        assertThat(proposalBack.getAppUser()).isEqualTo(appUser);

        appUser.setProposals(new HashSet<>());
        assertThat(appUser.getProposals()).doesNotContain(proposalBack);
        assertThat(proposalBack.getAppUser()).isNull();
    }
}
