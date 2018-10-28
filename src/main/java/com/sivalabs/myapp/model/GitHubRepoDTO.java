package com.sivalabs.myapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepoDTO {

    private Long id;
    private String name;
    private String description;

    @JsonProperty("html_url")
    private String url;

    private String language;
    private int forks;

    @JsonProperty("stargazers_count")
    private int stars;
}
