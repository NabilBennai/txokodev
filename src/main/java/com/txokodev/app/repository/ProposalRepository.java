package com.txokodev.app.repository;

import com.txokodev.app.domain.Proposal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Proposal entity.
 */
@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long>, JpaSpecificationExecutor<Proposal> {
    default Optional<Proposal> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Proposal> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Proposal> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select proposal from Proposal proposal left join fetch proposal.appUser left join fetch proposal.projectIdea",
        countQuery = "select count(proposal) from Proposal proposal"
    )
    Page<Proposal> findAllWithToOneRelationships(Pageable pageable);

    @Query("select proposal from Proposal proposal left join fetch proposal.appUser left join fetch proposal.projectIdea")
    List<Proposal> findAllWithToOneRelationships();

    @Query(
        "select proposal from Proposal proposal left join fetch proposal.appUser left join fetch proposal.projectIdea where proposal.id =:id"
    )
    Optional<Proposal> findOneWithToOneRelationships(@Param("id") Long id);
}
