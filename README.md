# spring-boot-2-sample

## Run tests

`> ./mvnw clean verify`

## Run application locally

`> ./mvnw clean spring-boot:run`

## Running using Docker

To start application and Postgres

`> ./run.sh start`


To start application and all dependent services like ELK, grafana, prometheus

`> ./run.sh start_all`

* Application: http://localhost:18080/
* SwaggerUI: http://localhost:18080/swagger-ui.html
* Prometheus: http://localhost:9090/
* Grafana: http://localhost:3000/ (admin/admin)
* Kibana: http://localhost:5601/ 

### Database migration

`./mvnw compile flyway:migrate`

### Run Performance Tests

`performance-tests> ./gradlew gatlingRun`

### Run SonarQube analysis

```
> ./run.sh sonar
> ./mvnw clean verify -P sonar -Dsonar.login=$SONAR_LOGIN_TOKEN
```

## References

* https://spring.io/projects/spring-boot
* https://start.spring.io/
* https://prometheus.io/
* https://grafana.com/
* https://docs.docker.com/
* https://jenkins.io/doc/
* https://www.elastic.co/elk-stack
* https://gatling.io/
* https://www.sonarqube.org/
* https://sonarcloud.io/
