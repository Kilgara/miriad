package com.truesoft.miriad.coreservice.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.truesoft.miriad.apicore.api.dto.user.CredentialsDto;
import com.truesoft.miriad.apicore.api.dto.user.request.UserCreateRequest;
import com.truesoft.miriad.apicore.api.dto.user.request.UserUpdateRequest;
import com.truesoft.miriad.coreservice.user.domain.Role;
import com.truesoft.miriad.coreservice.user.domain.User;
import com.truesoft.miriad.coreservice.user.domain.UserRole;
import com.truesoft.miriad.coreservice.user.exception.RoleNotFoundException;
import com.truesoft.miriad.coreservice.user.repository.RoleRepository;
import com.truesoft.miriad.coreservice.user.repository.UserRepository;

import static java.lang.String.format;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getUser() throws Exception {
        String uuid = UUID.randomUUID().toString();
        User testUser = createTestUser(uuid);

        String expectedJson = format("{\"url\":\"/user/0000/%s\",\"firstName\":\"firstName\",\"lastName\":\"lastName\","
            + "\"email\":\"%s@gmail.com\",\"roles\":[\"ROLE_USER\"]}", uuid, uuid);

        MockHttpServletRequestBuilder requestBuilder = get("/api/users/0000/" + testUser.getUuid());

        mvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedJson));
    }

    @Test
    public void finByEmail() throws Exception {
        String uuid = UUID.randomUUID().toString();
        User testUser = createTestUser(uuid);

        String expectedJson = format("{\"url\":\"/user/0000/%s\",\"firstName\":\"firstName\",\"lastName\":\"lastName\","
            + "\"email\":\"%s@gmail.com\",\"roles\":[\"ROLE_USER\"]}", uuid, uuid);

        MockHttpServletRequestBuilder requestBuilder = get("/api/users/0000/findBy")
            .param("email", testUser.getEmail());

        mvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedJson));
    }

    @Test
    public void finByCredentials() throws Exception {
        String uuid = UUID.randomUUID().toString();
        String password = "testPass";
        User testUser = createTestUser(uuid, password);
        CredentialsDto credentials = new CredentialsDto(testUser.getEmail(), password);

        String expectedJson = format("{\"url\":\"/user/0000/%s\",\"firstName\":\"firstName\",\"lastName\":\"lastName\","
            + "\"email\":\"%s@gmail.com\",\"roles\":[\"ROLE_USER\"]}", uuid, uuid);

        MockHttpServletRequestBuilder requestBuilder = post("/api/users/0000/findByCredentials")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(credentials));

        mvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedJson));
    }

    @Test
    public void create() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("firstName");
        request.setLastName("lastName");
        request.setEmail("createTest@gmail.com");
        request.setPassword("password");
        request.setRoles(Stream.of(UserRole.ROLE_USER.name()).collect(Collectors.toSet()));

        MockHttpServletRequestBuilder requestBuilder = put("/api/users/0000")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request));

        mvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.firstName", is("firstName")))
            .andExpect(jsonPath("$.lastName", is("lastName")))
            .andExpect(jsonPath("$.email", is("createTest@gmail.com")))
            .andExpect(jsonPath("$.roles", hasSize(1)))
            .andExpect(jsonPath("$.roles", hasItem("ROLE_USER")));
    }

    @Test
    public void update() throws Exception {
        String uuid = UUID.randomUUID().toString();
        User testUser = createTestUser(uuid);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("firstNameUpdated");
        request.setLastName("lastNameUpdated");
        request.setPassword("passwordUpdated");
        request.setRoles(Stream.of(UserRole.ROLE_USER.name(), UserRole.ROLE_ADMIN.name()).collect(Collectors.toSet()));

        MockHttpServletRequestBuilder requestBuilder = post("/api/users/0000/" + testUser.getUuid())
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request));

        mvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.firstName", is("firstNameUpdated")))
            .andExpect(jsonPath("$.lastName", is("lastNameUpdated")))
            .andExpect(jsonPath("$.email", is(testUser.getEmail())))
            .andExpect(jsonPath("$.roles", hasSize(2)))
            .andExpect(jsonPath("$.roles", hasItem("ROLE_USER")))
            .andExpect(jsonPath("$.roles", hasItem("ROLE_ADMIN")));
    }

    private User createTestUser(String uuid, String password) {
        Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
            .orElseThrow(() -> new RoleNotFoundException("There is no such role, name=" + UserRole.ROLE_USER.name()));

        User user = new User();
        user.setUuid(uuid);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(uuid + "@gmail.com");
        user.setPassword(encoder.encode(password));
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        return userRepository.save(user);
    }

    private User createTestUser(String uuid) {
        return createTestUser(uuid, "password");
    }
}
