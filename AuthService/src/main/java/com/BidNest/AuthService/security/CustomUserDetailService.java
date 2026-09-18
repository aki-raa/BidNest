package com.BidNest.AuthService.security;

import com.BidNest.AuthService.model.UserModel;
import com.BidNest.AuthService.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== Loading user: " + email);

        UserModel user = authRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("=== Found: " + user.getEmail());
        System.out.println("=== Role: " + user.getRole());
        System.out.println("=== Password: " + user.getPassword());

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
