package org.belligolifoundation.depliantsmanager.service;

/**
 * Depliant ACL service
 */
public interface DepliantAclService {

    /**
     * Decide if user can access to depliant
     *
     * @param id The id of depliant
     * @return True if user can access, false otherwise
     */
    boolean canAccess(Long id);
}
