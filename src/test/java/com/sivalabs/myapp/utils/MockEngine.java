package com.sivalabs.myapp.utils;

import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;

import static com.sivalabs.myapp.utils.TestHelper.getClasspathResourceContent;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

public class MockEngine {

    private static final String mockResponsesDir = "/mockserver/responses";
    private static final String GET_GITHUB_USER_RESPONSE = mockResponse("/get-github-user-response.json");
    private static final String GET_GITHUB_USER_REPOS_RESPONSE = mockResponse("/get-github-user-repos-response.json");

    private final MockServerClient mockServerClient;

    public MockEngine(MockServerClient mockServerClient) {
        this.mockServerClient = mockServerClient;
    }

    private static String mockResponse(String file) {
        return getClasspathResourceContent(mockResponsesDir + file);
    }

    public void mockGetGithubUser(String username) {
        HttpRequest request = request("/users/" + username);
        mockServerClient
            .when(request)
            .respond(
                response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody(json(GET_GITHUB_USER_RESPONSE)));
    }

    public void mockGetGithubUserRepos(String username) {
        HttpRequest request = request("/users/" + username + "/repos");
        mockServerClient
            .when(request)
            .respond(
                response()
                    .withHeader(Header.header("Content-Type", "application/json"))
                    .withBody(json(GET_GITHUB_USER_REPOS_RESPONSE)));
    }
}
