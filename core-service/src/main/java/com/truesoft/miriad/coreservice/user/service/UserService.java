package com.truesoft.miriad.coreservice.user.service;

import java.util.UUID;

import com.truesoft.miriad.coreservice.user.domain.User;
import com.truesoft.miriad.coreservice.user.exception.RoleNotFoundException;
import com.truesoft.miriad.coreservice.user.exception.UserAlreadyExistsException;
import com.truesoft.miriad.coreservice.user.exception.UserNotFoundException;

public interface UserService {

    /**
     * Creates a new User
     * @param user to create
     * @return created user
     * @throws UserAlreadyExistsException if there is such user
     * @throws RoleNotFoundException if list or roles contains invalid role
     */
    User create(User user);

    /**
     * Updates existing User
     * @param user - user data
     * @return updated user
     * @throws UserNotFoundException if there is no user to update
     * @throws RoleNotFoundException if list or roles contains invalid role
     */
    User update(UUID uuid, User user);

    /**
     * Returns user entity by UUID
     * @param uuid
     * @return user entity
     * @throws UserNotFoundException if there is no such user
     */
    User get(UUID uuid);

    /**
     * Returns user entity by email
     * @param email
     * @return user entity
     * @throws UserNotFoundException if there is no such user
     */
    User findByEmail(String email);

    /**
     * Returns user entity by email and password
     * @param email
     * @param password
     * @return user entity
     * @throws UserNotFoundException if there is no such user
     */
    User findByEmailAndPassword(String email, String password);
}
