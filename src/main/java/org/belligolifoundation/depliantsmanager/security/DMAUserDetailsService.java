package org.belligolifoundation.depliantsmanager.security;

import org.belligolifoundation.depliantsmanager.model.dto.UserDTO;
import org.belligolifoundation.depliantsmanager.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class DMAUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public DMAUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new DMAUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities(), user.getEmail());
    }

    public Collection<GrantedAuthority> authorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }
}
