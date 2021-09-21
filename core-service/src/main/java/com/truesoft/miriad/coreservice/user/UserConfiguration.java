package com.truesoft.miriad.coreservice.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.truesoft.miriad.coreservice.user.repository.RoleRepository;
import com.truesoft.miriad.coreservice.user.repository.UserRepository;
import com.truesoft.miriad.coreservice.user.service.UserService;
import com.truesoft.miriad.coreservice.user.service.UserServiceImpl;

@Configuration
public class UserConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
    }
}
