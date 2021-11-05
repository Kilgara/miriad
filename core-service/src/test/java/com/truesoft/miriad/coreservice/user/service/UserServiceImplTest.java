package com.truesoft.miriad.coreservice.user.service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.truesoft.miriad.coreservice.user.domain.Role;
import com.truesoft.miriad.coreservice.user.domain.User;
import com.truesoft.miriad.coreservice.user.domain.UserRole;
import com.truesoft.miriad.coreservice.user.exception.RoleNotFoundException;
import com.truesoft.miriad.coreservice.user.exception.UserAlreadyExistsException;
import com.truesoft.miriad.coreservice.user.repository.RoleRepository;
import com.truesoft.miriad.coreservice.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void create() {
        User user = simpleUser();

        when(userRepository.findByEmail(eq(user.getEmail())))
            .thenReturn(Optional.empty());
        when(passwordEncoder.encode(any()))
            .thenReturn("password");
        when(roleRepository.findByName(eq(UserRole.ROLE_USER)))
            .thenReturn(Optional.of(new Role(UserRole.ROLE_USER)));

        userService.create(user);

        verify(passwordEncoder).encode(eq("password"));
        verify(userRepository).findByEmail(eq(user.getEmail()));
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void create_invalidRole() {
        assertThrows(RoleNotFoundException.class, () -> {
            User user = simpleUser();

            when(userRepository.findByEmail(eq(user.getEmail())))
                .thenReturn(Optional.empty());
            when(passwordEncoder.encode(any()))
                .thenReturn("password");
            when(roleRepository.findByName(any(UserRole.class)))
                .thenReturn(Optional.empty());

            userService.create(user);

            verify(passwordEncoder).encode(eq("password"));
            verify(userRepository).findByEmail(eq(user.getEmail()));
            verify(userRepository, never()).save(any(User.class));
        });
    }

    @Test
    public void create_userAlreadyExists() {
        assertThrows(UserAlreadyExistsException.class, () -> {
            User user = simpleUser();

            when(userRepository.findByEmail(eq(user.getEmail())))
                .thenReturn(Optional.of(user));

            userService.create(user);

            verify(passwordEncoder, never()).encode(eq("password"));
            verify(userRepository, never()).findByEmail(eq(user.getEmail()));
            verify(userRepository, never()).save(any(User.class));
        });
    }

    @Test
    public void update() {
        User userToUpdate = simpleUser();
        userToUpdate.setUuid(UUID.randomUUID().toString());

        User user = simpleUser();
        user.setFirstName("fistNameUpdated");
        user.setLastName("lastNameUpdated");
        user.setEmail("testUpdated@gmail.com");
        user.setPassword("passwordUpdated");
        user.setRoles(Stream.of(new Role(UserRole.ROLE_USER), new Role(UserRole.ROLE_ADMIN)).collect(Collectors.toSet()));

        when(userRepository.findByUuid(userToUpdate.getUuid()))
            .thenReturn(Optional.of(userToUpdate));
        when(passwordEncoder.encode(eq(user.getPassword())))
            .thenReturn("passwordUpdated");
        when(roleRepository.findByName(eq(UserRole.ROLE_USER)))
            .thenReturn(Optional.of(new Role(UserRole.ROLE_USER)));
        when(roleRepository.findByName(eq(UserRole.ROLE_ADMIN)))
            .thenReturn(Optional.of(new Role(UserRole.ROLE_ADMIN)));

        User updatedUser = userService.update(UUID.fromString(userToUpdate.getUuid()), user);

        assertNotNull(updatedUser);
        assertEquals(user.getFirstName(), updatedUser.getFirstName());
        assertEquals(user.getLastName(), updatedUser.getLastName());
        assertNotEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getPassword(), updatedUser.getPassword());
        assertEquals(user.getRoles(), updatedUser.getRoles());
    }

    private User simpleUser() {
        User user = new User();
        user.setFirstName("fistName");
        user.setLastName("lastName");
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        user.setRoles(Stream.of(new Role(UserRole.ROLE_USER)).collect(Collectors.toSet()));

        return user;
    }
}
