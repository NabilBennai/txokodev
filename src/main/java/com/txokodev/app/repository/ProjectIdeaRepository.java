package com.txokodev.app.repository;

import com.txokodev.app.domain.ProjectIdea;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProjectIdea entity.
 */
@Repository
public interface ProjectIdeaRepository extends JpaRepository<ProjectIdea, Long>, JpaSpecificationExecutor<ProjectIdea> {
    default Optional<ProjectIdea> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProjectIdea> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProjectIdea> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select projectIdea from ProjectIdea projectIdea left join fetch projectIdea.appUser",
        countQuery = "select count(projectIdea) from ProjectIdea projectIdea"
    )
    Page<ProjectIdea> findAllWithToOneRelationships(Pageable pageable);

    @Query("select projectIdea from ProjectIdea projectIdea left join fetch projectIdea.appUser")
    List<ProjectIdea> findAllWithToOneRelationships();

    @Query("select projectIdea from ProjectIdea projectIdea left join fetch projectIdea.appUser where projectIdea.id =:id")
    Optional<ProjectIdea> findOneWithToOneRelationships(@Param("id") Long id);
}
