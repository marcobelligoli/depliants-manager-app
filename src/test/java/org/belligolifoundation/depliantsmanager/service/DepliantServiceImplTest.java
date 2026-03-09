package org.belligolifoundation.depliantsmanager.service;

import org.belligolifoundation.depliantsmanager.exception.DepliantNotFoundException;
import org.belligolifoundation.depliantsmanager.model.Depliant;
import org.belligolifoundation.depliantsmanager.model.dto.DepliantDTO;
import org.belligolifoundation.depliantsmanager.repository.DepliantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepliantServiceImplTest {

    private static final long DEPLIANT_ID = 1L;
    private static final int DEPLIANT_NUMBER = 123;
    private static final String DEPLIANT_DESCRIPTION = "desc";
    private static final String SEARCH = "test";

    @InjectMocks
    private DepliantServiceImpl service;

    @Mock
    private DepliantRepository depliantRepository;

    @Test
    void getDepliantsByUser_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Depliant depliant = new Depliant();
        depliant.setId(DEPLIANT_ID);

        Page<Depliant> page = new PageImpl<>(List.of(depliant));

        when(depliantRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<DepliantDTO> result = service.getDepliantsByUser(DEPLIANT_ID, pageable, SEARCH);

        assertEquals(1, result.getTotalElements());
        verify(depliantRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getDepliantById_shouldReturnDTO() {

        Depliant depliant = new Depliant();
        depliant.setId(DEPLIANT_ID);

        when(depliantRepository.findById(DEPLIANT_ID)).thenReturn(Optional.of(depliant));

        DepliantDTO result = service.getDepliantById(DEPLIANT_ID);

        assertNotNull(result);
        assertEquals(DEPLIANT_ID, result.getId());
    }

    @Test
    void getDepliantById_shouldThrowExceptionWhenNotFound() {

        when(depliantRepository.findById(DEPLIANT_ID)).thenReturn(Optional.empty());

        assertThrows(
                DepliantNotFoundException.class,
                () -> service.getDepliantById(DEPLIANT_ID)
        );
    }

    @Test
    void saveDepliant_shouldSaveAndReturnDTO() {

        DepliantDTO dto = new DepliantDTO();
        dto.setId(DEPLIANT_ID);

        Depliant saved = new Depliant();
        saved.setId(DEPLIANT_ID);

        when(depliantRepository.save(any())).thenReturn(saved);

        DepliantDTO result = service.saveDepliant(dto);

        assertNotNull(result);
        assertEquals(DEPLIANT_ID, result.getId());

        verify(depliantRepository).save(any());
    }

    @Test
    void updateDepliant_shouldUpdateFields() {

        Depliant existing = new Depliant();
        existing.setId(DEPLIANT_ID);

        DepliantDTO dto = new DepliantDTO();
        dto.setId(DEPLIANT_ID);
        dto.setNumber(DEPLIANT_NUMBER);
        dto.setDescription(DEPLIANT_DESCRIPTION);

        when(depliantRepository.findById(DEPLIANT_ID)).thenReturn(Optional.of(existing));
        when(depliantRepository.save(existing)).thenReturn(existing);

        DepliantDTO result = service.updateDepliant(dto);

        assertEquals(DEPLIANT_NUMBER, existing.getNumber());
        assertEquals(DEPLIANT_DESCRIPTION, existing.getDescription());

        verify(depliantRepository).save(existing);
        assertNotNull(result);
    }

    @Test
    void updateDepliant_shouldThrowExceptionWhenNotFound() {

        DepliantDTO dto = new DepliantDTO();
        dto.setId(DEPLIANT_ID);

        when(depliantRepository.findById(DEPLIANT_ID)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateDepliant(dto)
        );
    }

    @Test
    void deleteDepliant_shouldCallRepository() {

        service.deleteDepliant(DEPLIANT_ID);

        verify(depliantRepository).deleteById(DEPLIANT_ID);
    }
}
