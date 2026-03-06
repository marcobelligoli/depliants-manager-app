package org.belligolifoundation.depliantsmanager.service;

import org.belligolifoundation.depliantsmanager.repository.DepliantRepository;
import org.belligolifoundation.depliantsmanager.security.CustomUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DepliantAclServiceImpl implements DepliantAclService {

    private final DepliantRepository depliantRepository;

    public DepliantAclServiceImpl(DepliantRepository depliantRepository) {
        this.depliantRepository = depliantRepository;
    }

    @Override
    public boolean canAccess(Long id) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return depliantRepository.existsByIdAndUserId(id, userDetails.getUserId());
    }
}
