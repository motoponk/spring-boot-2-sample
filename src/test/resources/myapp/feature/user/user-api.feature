Feature: User API

  Background:
    * url baseUrl

  Scenario: Fetch all users

    Given path '/api/users'
    When method GET
    Then status 200

  Scenario: Fetch user by id

    Given path '/api/users/10'
    When method GET
    Then status 200

