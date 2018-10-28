package com.sivalabs.myapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubUserDTO {

    private Long id;
    private String name;
    private String url;

    @JsonProperty("public_repos")
    private int publicRepos;

    private List<GitHubRepoDTO> repos;
}
