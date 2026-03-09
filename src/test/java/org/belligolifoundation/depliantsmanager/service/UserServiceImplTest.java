package org.belligolifoundation.depliantsmanager.service;

import org.belligolifoundation.depliantsmanager.model.User;
import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.belligolifoundation.depliantsmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String USERNAME = "testuser";
    private static final String PLAIN_PASSWORD = "plainPassword";
    private static final long USER_ID = 1L;
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_shouldEncodePasswordAndSaveUser() {

        UserDTO dto = new UserDTO();
        dto.setUsername(USERNAME);
        dto.setPassword(PLAIN_PASSWORD);

        User savedUser = new User();
        savedUser.setId(USER_ID);
        savedUser.setUsername(USERNAME);
        savedUser.setPassword(ENCODED_PASSWORD);

        when(passwordEncoder.encode(PLAIN_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = service.registerUser(dto);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());

        verify(passwordEncoder).encode(PLAIN_PASSWORD);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findByUsername_shouldReturnUserDTO() {

        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);

        when(userRepository.findByUsername(USERNAME)).thenReturn(user);

        UserDTO result = service.findByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());

        verify(userRepository).findByUsername(USERNAME);
    }
}
