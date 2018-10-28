package com.sivalabs.myapp.utils;

import com.sivalabs.myapp.model.UserDTO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static java.lang.String.format;

public class TestHelper {

    public static UserDTO buildUser() {
        String uuid = UUID.randomUUID().toString();
        return UserDTO.builder()
            .name("name-" + uuid)
            .email(format("someone-%s@gmail.com", uuid))
            .githubUsername("ghuser-" + uuid)
            .build();
    }

    public static UserDTO buildUserWithId() {
        Random random = new Random();
        String uuid = UUID.randomUUID().toString();
        return UserDTO.builder()
            .id(random.nextLong())
            .name("name-" + uuid)
            .email(format("someone-%s@gmail.com", uuid))
            .githubUsername("ghuser-" + uuid)
            .build();
    }

    public static String getClasspathResourceContent(String filepath) {
        try {
            Path path = Paths.get(TestHelper.class.getClass().getResource(filepath).toURI());

            StringBuilder data = new StringBuilder();
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> data.append(line).append("\n"));
            lines.close();
            return data.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
