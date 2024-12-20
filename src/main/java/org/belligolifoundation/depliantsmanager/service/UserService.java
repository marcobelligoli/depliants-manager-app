package org.belligolifoundation.depliantsmanager.service;

import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;

/**
 * User service
 */
public interface UserService {

    /**
     * Register a new user
     *
     * @param userDTO User to be registered
     * @return User registered
     */
    UserDTO registerUser(UserDTO userDTO);

    /**
     * Find user by username
     *
     * @param username Username
     * @return User found
     */
    UserDTO findByUsername(String username);
}
