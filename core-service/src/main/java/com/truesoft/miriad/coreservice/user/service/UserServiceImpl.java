package com.truesoft.miriad.coreservice.user.service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.truesoft.miriad.coreservice.user.domain.Role;
import com.truesoft.miriad.coreservice.user.domain.User;
import com.truesoft.miriad.coreservice.user.domain.UserRole;
import com.truesoft.miriad.coreservice.user.exception.RoleNotFoundException;
import com.truesoft.miriad.coreservice.user.exception.UserAlreadyExistsException;
import com.truesoft.miriad.coreservice.user.exception.UserNotFoundException;
import com.truesoft.miriad.coreservice.user.repository.RoleRepository;
import com.truesoft.miriad.coreservice.user.repository.UserRepository;

import static java.util.Objects.nonNull;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public User create(User user) {
        if (userExists(user.getEmail())) {
            throw new UserAlreadyExistsException("User with such email already exists, email=" + user.getEmail());
        }

        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(getRoles(user.getRoles()));

        return userRepository.save(user);
    }

    private boolean userExists(String email) {
        return userRepository.findByEmail(email)
            .isPresent();
    }

    @Override
    public User update(UUID uuid, User user) {
        final User userToUpdate = get(uuid);

        return userRepository.save(merge(user, userToUpdate));
    }

    @Override
    public User get(UUID uuid) {
        return userRepository.findByUuid(uuid.toString())
            .orElseThrow(() -> new UserNotFoundException("User with such uuid doesn't exist, uuid=" + uuid.toString()));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User with such email doesn't exist, email=" + email));
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        final User user = findByEmail(email);

        if (!encoder.matches(password, user.getPassword())) {
            throw new UserNotFoundException("User with such password doesn't exist.");
        }

        return user;
    }

    private User merge(User source, User master) {
        if (nonNull(source.getFirstName())) {
            master.setFirstName(source.getFirstName());
        }
        if (nonNull(source.getLastName())) {
            master.setLastName(source.getLastName());
        }
        if (nonNull(source.getPassword())) {
            master.setPassword(encoder.encode(source.getPassword()));
        }
        if (nonNull(source.getRoles())) {
            master.setRoles(getRoles(source.getRoles()));
        }

        return master;
    }

    private Role roleFindByNameOrThrowException(UserRole userRole) {
        return roleRepository.findByName(userRole)
            .orElseThrow(() -> new RoleNotFoundException("There is no such role, name=" + userRole.name()));
    }

    private Set<Role> getRoles(Set<Role> roles) {
        return roles.stream()
            .map(Role::getName)
            .map(this::roleFindByNameOrThrowException)
            .collect(Collectors.toSet());
    }
}
