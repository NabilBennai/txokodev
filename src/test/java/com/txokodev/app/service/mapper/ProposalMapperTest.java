package com.txokodev.app.service.mapper;

import static com.txokodev.app.domain.ProposalAsserts.*;
import static com.txokodev.app.domain.ProposalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProposalMapperTest {

    private ProposalMapper proposalMapper;

    @BeforeEach
    void setUp() {
        proposalMapper = new ProposalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProposalSample1();
        var actual = proposalMapper.toEntity(proposalMapper.toDto(expected));
        assertProposalAllPropertiesEquals(expected, actual);
    }
}
