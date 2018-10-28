package com.sivalabs.myapp.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sivalabs.myapp.config.Loggable;
import com.sivalabs.myapp.model.GitHubRepoDTO;
import com.sivalabs.myapp.model.GitHubUserDTO;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Loggable
@Slf4j
public class GithubClient {

    private final RestTemplate restTemplate;

    @Value("${github.host}")
    String githubApiBasePath;

    @Autowired
    public GithubClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(
        fallbackMethod = "getDefaultUser",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
        })
    @Timed("sb2s.githubclient.getuser")
    public GitHubUserDTO getUser(final String username) {
        log.info("process=get-github-user, username=" + username);
        GitHubUserDTO userInfo = getUserInfo(username);
        if (userInfo != null) {
            userInfo.setRepos(getUserRepos(username));
        }
        return userInfo;
    }

    @SuppressWarnings("unused")
    GitHubUserDTO getDefaultUser(final String username) {
        log.info("process=get-github-default-user, username=" + username);
        return GitHubUserDTO.builder().name(username).repos(Collections.emptyList()).build();
    }

    private GitHubUserDTO getUserInfo(final String username) {
        return restTemplate.getForObject(githubApiBasePath + "/users/" + username, GitHubUserDTO.class);
    }

    private List<GitHubRepoDTO> getUserRepos(final String username) {
        GitHubRepoDTO[] repos =
            restTemplate.getForObject(
                githubApiBasePath + "/users/" + username + "/repos", GitHubRepoDTO[].class);
        return Arrays.asList(repos);
    }
}
