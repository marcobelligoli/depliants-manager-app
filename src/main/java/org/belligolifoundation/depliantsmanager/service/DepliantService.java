package org.belligolifoundation.depliantsmanager.service;

import org.belligolifoundation.depliantsmanager.model.dto.DepliantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Depliant service
 */
public interface DepliantService {

    /**
     * Get depliants for user
     *
     * @param userId   The user id
     * @param pageable Pagination info
     * @param search   Search filters
     * @return Depliants found
     */
    Page<DepliantDTO> getDepliantsByUser(Long userId, Pageable pageable, String search);

    /**
     * Get depliant by id
     *
     * @param id The id of depliant
     * @return Depliant found
     */
    DepliantDTO getDepliantById(Long id);

    /**
     * Save a new depliant
     *
     * @param depliantDTO Depliant to save
     * @return Depliant saved
     */
    DepliantDTO saveDepliant(DepliantDTO depliantDTO);

    /**
     * Update an existing depliant
     *
     * @param updatedDepliantDTO Depliant to update
     * @return Depliant updated
     */
    DepliantDTO updateDepliant(DepliantDTO updatedDepliantDTO);

    /**
     * Delete a depliant
     *
     * @param depliantId Depliant id to delete
     */
    void deleteDepliant(Long depliantId);
}
