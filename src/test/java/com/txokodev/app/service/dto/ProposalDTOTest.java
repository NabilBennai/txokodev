package com.txokodev.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.txokodev.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProposalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProposalDTO.class);
        ProposalDTO proposalDTO1 = new ProposalDTO();
        proposalDTO1.setId(1L);
        ProposalDTO proposalDTO2 = new ProposalDTO();
        assertThat(proposalDTO1).isNotEqualTo(proposalDTO2);
        proposalDTO2.setId(proposalDTO1.getId());
        assertThat(proposalDTO1).isEqualTo(proposalDTO2);
        proposalDTO2.setId(2L);
        assertThat(proposalDTO1).isNotEqualTo(proposalDTO2);
        proposalDTO1.setId(null);
        assertThat(proposalDTO1).isNotEqualTo(proposalDTO2);
    }
}
