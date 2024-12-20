package org.belligolifoundation.depliantsmanager.service;

import org.belligolifoundation.depliantsmanager.model.User;
import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.belligolifoundation.depliantsmanager.repository.UserRepository;
import org.belligolifoundation.depliantsmanager.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        logger.debug("Saving new user [{}]...", userDTO.getUsername());
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserMapper.INSTANCE.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO findByUsername(String username) {
        logger.debug("Searching user with username [{}]...", username);
        return UserMapper.INSTANCE.toDTO(userRepository.findByUsername(username));
    }
}
