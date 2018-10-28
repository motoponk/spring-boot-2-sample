package com.sivalabs.myapp.web.controller;

import com.sivalabs.myapp.model.UserDTO;
import com.sivalabs.myapp.model.UserProfile;
import com.sivalabs.myapp.repo.UserRepository;
import com.sivalabs.myapp.utils.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class UserControllerIT extends AbstractIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestRestTemplate restTemplate;

    UserDTO existingUser, newUser, updateUser;

    @Before
    public void setUp() {
        super.setUp();
        newUser = TestHelper.buildUser();

        existingUser = TestHelper.buildUser();
        existingUser = UserDTO.fromEntity(userRepository.save(existingUser.toEntity()));

        updateUser = TestHelper.buildUser();
        updateUser = UserDTO.fromEntity(userRepository.save(updateUser.toEntity()));
    }

    @After
    public void tearDown() {
        super.tearDown();
        if (newUser.getId() != null) {
            userRepository.deleteById(newUser.getId());
        }
        userRepository.deleteAll(
            userRepository.findAllById(asList(existingUser.getId(), updateUser.getId())));
    }

    @Test
    public void should_get_all_users() {
        ResponseEntity<UserDTO[]> responseEntity =
            restTemplate.getForEntity("/api/users", UserDTO[].class);
        List<UserDTO> users = asList(responseEntity.getBody());
        assertThat(users).isNotEmpty();
    }

    @Test
    public void should_get_user_by_id() {
        mockEngine.mockGetGithubUser(existingUser.getGithubUsername());
        mockEngine.mockGetGithubUserRepos(existingUser.getGithubUsername());

        ResponseEntity<UserProfile> responseEntity =
            restTemplate.getForEntity("/api/users/" + existingUser.getId(), UserProfile.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserProfile user = responseEntity.getBody();
        assertThat(user).isNotNull();
    }

    @Test
    public void should_create_user() {
        HttpEntity<UserDTO> request = new HttpEntity<>(newUser);
        ResponseEntity<UserDTO> responseEntity =
            restTemplate.postForEntity("/api/users", request, UserDTO.class);
        UserDTO savedUser = responseEntity.getBody();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void should_update_user() {
        HttpEntity<UserDTO> request = new HttpEntity<>(updateUser);
        restTemplate.put("/api/users/" + updateUser.getId(), request, UserDTO.class);
        ResponseEntity<UserDTO> responseEntity =
            restTemplate.getForEntity("/api/users/" + updateUser.getId(), UserDTO.class);
        UserDTO updatedUser = responseEntity.getBody();
        assertThat(updatedUser.getId()).isEqualTo(updateUser.getId());
        assertThat(updatedUser.getEmail()).isEqualTo(updateUser.getEmail());
    }

    @Test
    public void should_delete_user() {
        ResponseEntity<UserDTO> responseEntity =
            restTemplate.getForEntity("/api/users/" + existingUser.getId(), UserDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        restTemplate.delete("/api/users/" + existingUser.getId());
        responseEntity = restTemplate.getForEntity("/api/users/" + existingUser.getId(), UserDTO.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}
