package com.sivalabs.myapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.myapp.model.UserDTO;
import com.sivalabs.myapp.model.UserProfile;
import com.sivalabs.myapp.repo.UserRepository;
import com.sivalabs.myapp.utils.TestHelper;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerRestAssuredIT extends AbstractIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    UserDTO existingUser, newUser, updateUser;

    @LocalServerPort
    private int serverPort;

    @Before
    public void setUp() {
        super.setUp();
        RestAssured.baseURI = "http://localhost:" + serverPort;

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
        UserDTO[] response =
            given().when().get("/api/users").then().statusCode(200).extract().as(UserDTO[].class);
        List<UserDTO> users = asList(response);
        assertThat(users).isNotEmpty();
    }

    @Test
    public void should_get_user_by_id() {
        mockEngine.mockGetGithubUser(existingUser.getGithubUsername());
        mockEngine.mockGetGithubUserRepos(existingUser.getGithubUsername());

        UserProfile user =
            given()
                .when()
                .get("/api/users/" + existingUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserProfile.class);

        assertThat(user).isNotNull();
    }

    @Test
    public void should_create_user() {
        UserDTO savedUser =
            given()
                .contentType("application/json")
                .body(newUser)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .as(UserDTO.class);
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void should_update_user() {
        UserDTO updatedUser =
            given()
                .contentType("application/json")
                .body(updateUser)
                .when()
                .put("/api/users/" + updateUser.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(UserDTO.class);

        assertThat(updatedUser.getId()).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(updateUser.getId());
        assertThat(updatedUser.getEmail()).isEqualTo(updateUser.getEmail());
    }

    @Test
    public void should_delete_user() {

        given().when().get("/api/users/" + existingUser.getId()).then().statusCode(200);

        given().when().delete("/api/users/" + existingUser.getId()).then().statusCode(200);
        given().when().get("/api/users/" + existingUser.getId()).then().statusCode(404);
    }
}
