package com.fho.housewarmingparty.security.authentication;

import java.util.ArrayList;

import com.fho.housewarmingparty.api.user.entity.User;
import com.fho.housewarmingparty.api.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(user.getId(), user.getName(), user.getEmail(), user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}