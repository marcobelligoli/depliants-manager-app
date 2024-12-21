package org.belligolifoundation.depliantsmanager.repository;

import org.belligolifoundation.depliantsmanager.model.Depliant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DepliantRepository extends JpaRepository<Depliant, Long>, JpaSpecificationExecutor<Depliant> {
}
