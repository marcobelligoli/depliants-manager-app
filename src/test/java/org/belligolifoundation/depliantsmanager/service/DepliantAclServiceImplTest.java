package org.belligolifoundation.depliantsmanager.service;

import org.belligolifoundation.depliantsmanager.repository.DepliantRepository;
import org.belligolifoundation.depliantsmanager.security.DMAUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepliantAclServiceImplTest {

    private static final long DEPLIANT_ID = 1L;
    private static final long USER_ID = 10L;
    private static final String USERNAME = "test";
    private static final String PASSWORD = "password";
    private static final List<SimpleGrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));
    private static final String EMAIL = "mail@localhost";

    @InjectMocks
    private DepliantAclServiceImpl service;

    @Mock
    private DepliantRepository depliantRepository;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnFalseWhenAuthenticationIsNull() {

        SecurityContextHolder.clearContext();

        boolean result = service.canAccess(DEPLIANT_ID);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenAnonymousUser() {

        var auth = mock(AnonymousAuthenticationToken.class);
        when(auth.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        boolean result = service.canAccess(DEPLIANT_ID);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenUserOwnsDepliant() {

        DMAUserDetails userDetails = new DMAUserDetails(USER_ID, USERNAME, PASSWORD, AUTHORITIES, EMAIL);
        var auth = new UsernamePasswordAuthenticationToken(userDetails, PASSWORD, AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(depliantRepository.existsByIdAndUserId(DEPLIANT_ID, USER_ID)).thenReturn(true);

        boolean result = service.canAccess(DEPLIANT_ID);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotOwnDepliant() {

        DMAUserDetails userDetails = new DMAUserDetails(USER_ID, USERNAME, PASSWORD, AUTHORITIES, EMAIL);
        var auth = new UsernamePasswordAuthenticationToken(userDetails, PASSWORD, AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(depliantRepository.existsByIdAndUserId(DEPLIANT_ID, USER_ID)).thenReturn(false);

        boolean result = service.canAccess(DEPLIANT_ID);

        assertFalse(result);
    }
}
