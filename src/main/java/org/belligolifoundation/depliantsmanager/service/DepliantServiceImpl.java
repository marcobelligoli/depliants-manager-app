package org.belligolifoundation.depliantsmanager.service;

import org.belligolifoundation.depliantsmanager.exception.DepliantNotFoundException;
import org.belligolifoundation.depliantsmanager.model.Depliant;
import org.belligolifoundation.depliantsmanager.model.dto.DepliantDTO;
import org.belligolifoundation.depliantsmanager.model.spec.DepliantSpecification;
import org.belligolifoundation.depliantsmanager.repository.DepliantRepository;
import org.belligolifoundation.depliantsmanager.service.mapper.DepliantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepliantServiceImpl implements DepliantService {

    private static final Logger logger = LoggerFactory.getLogger(DepliantServiceImpl.class);
    private final DepliantRepository depliantRepository;

    public DepliantServiceImpl(DepliantRepository depliantRepository) {
        this.depliantRepository = depliantRepository;
    }

    @Override
    public Page<DepliantDTO> getDepliantsByUser(Long userId, Pageable pageable, String search) {
        logger.debug("Searching depliant by user [{}]...", userId);
        Specification<Depliant> spec = Specification.allOf(
                DepliantSpecification.withUserId(userId),
                DepliantSpecification.search(search)
        );
        return depliantRepository.findAll(spec, pageable).map(DepliantMapper.INSTANCE::toDTO);
    }

    @Override
    public DepliantDTO getDepliantById(Long id) {
        logger.debug("Getting depliant by id [{}]...", id);
        var depliant = depliantRepository.findById(id).orElseThrow(() -> new DepliantNotFoundException(id));
        return DepliantMapper.INSTANCE.toDTO(depliant);
    }

    @Override
    public DepliantDTO saveDepliant(DepliantDTO depliantDTO) {
        logger.debug("Saving depliant [{}]...", depliantDTO);
        Depliant depliant = DepliantMapper.INSTANCE.toEntity(depliantDTO);
        return DepliantMapper.INSTANCE.toDTO(depliantRepository.save(depliant));
    }

    @Override
    public DepliantDTO updateDepliant(DepliantDTO updatedDepliantDTO) {
        logger.debug("Updating depliant [{}]...", updatedDepliantDTO.getId());
        Optional<Depliant> optionalDepliant = depliantRepository.findById(updatedDepliantDTO.getId());
        if (optionalDepliant.isPresent()) {
            Depliant existingDepliant = optionalDepliant.get();
            existingDepliant.setNumber(updatedDepliantDTO.getNumber());
            existingDepliant.setDescription(updatedDepliantDTO.getDescription());
            existingDepliant.setEventName(updatedDepliantDTO.getEventName());
            existingDepliant.setNotes(updatedDepliantDTO.getNotes());
            existingDepliant.setLanguage(updatedDepliantDTO.getLanguage());
            return DepliantMapper.INSTANCE.toDTO(depliantRepository.save(existingDepliant));
        } else {
            throw new IllegalArgumentException("Depliant not found");
        }
    }

    @Override
    public void deleteDepliant(Long depliantId) {
        logger.debug("Deleting depliant [{}]...", depliantId);
        depliantRepository.deleteById(depliantId);
    }
}
