package org.belligolifoundation.depliantsmanager.repository;

import org.belligolifoundation.depliantsmanager.model.Depliant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepliantRepository extends JpaRepository<Depliant, Long> {

    Page<Depliant> findByUserId(Long userId, Pageable pageable);
}
