package com.sivalabs.myapp.service;

import com.sivalabs.myapp.config.Loggable;
import com.sivalabs.myapp.entity.User;
import com.sivalabs.myapp.model.GitHubRepoDTO;
import com.sivalabs.myapp.model.GitHubUserDTO;
import com.sivalabs.myapp.model.UserDTO;
import com.sivalabs.myapp.model.UserProfile;
import com.sivalabs.myapp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@Loggable
public class UserService {

    private final UserRepository userRepository;
    private final GithubClient githubClient;

    @Autowired
    public UserService(UserRepository userRepository, GithubClient githubClient) {
        this.userRepository = userRepository;
        this.githubClient = githubClient;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                    .stream()
                    .map(UserDTO::fromEntity)
                    .collect(toList());
    }

    public UserDTO createUser(UserDTO user) {
        return UserDTO.fromEntity(userRepository.save(user.toEntity()));
    }

    public UserDTO updateUser(UserDTO user) {
        return UserDTO.fromEntity(userRepository.save(user.toEntity()));
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }

    public Optional<UserProfile> getUserProfile(Long id) {
        Optional<User> userOptional = this.getUserById(id);
        if (!userOptional.isPresent()) {
            return Optional.empty();
        }
        User user = userOptional.get();
        UserProfile profile = new UserProfile();
        profile.setId(user.getId());
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        if (user.getGithubUsername() != null) {
            GitHubUserDTO ghUserDTO = githubClient.getUser(user.getGithubUsername());
            if (ghUserDTO != null && ghUserDTO.getRepos() != null) {
                Collections.sort(
                        ghUserDTO.getRepos(), Comparator.comparingInt(GitHubRepoDTO::getStars).reversed());
            }
            profile.setGithubProfile(ghUserDTO);
        }
        return Optional.of(profile);
    }
}
