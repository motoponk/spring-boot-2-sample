package com.sivalabs.myapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfile {

    private Long id;
    private String name;
    private String email;
    private GitHubUserDTO githubProfile;
}
