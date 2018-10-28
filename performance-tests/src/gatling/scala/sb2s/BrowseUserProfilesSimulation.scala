package sb2s

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.util.Random


class BrowseUserProfilesSimulation extends Simulation {


  val httpConf = http
    .baseURL("http://localhost:18080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")


  val userIdFeeder = csv("userIds.csv").random
  val newUsersFeeder = csv("newUsers.csv").random


  var randomString = Iterator.continually(Map("randstring" -> ( Random.alphanumeric.take(20).mkString )))
  var randomInt = Iterator.continually(Map("randinteger" -> ( Random.nextInt(1000))))

  val getAllUsers = exec(http("AllUsers").get("/api/users")).pause(1)

  val getUserById = feed(randomInt)
               .exec(http("UserById").get("/api/users/${randinteger}"))
               .pause(1)

  val createUser = feed(randomString)
                    .exec(http("Create User")
                        .post("/api/users")
                        .body(StringBody(
                          """
                            { "name":"${randstring}",
                            "email":"${randstring}@gmail.com",
                            "githubUsername":"${randstring}"
                            }
                          """)).asJSON
                    )
                    .pause(1)

  val deleteUserById = feed(randomInt)
    .exec(http("DeleteUser").delete("/api/users/${randinteger}"))
    .pause(1)

  // Now, we can write the scenario as a composition
  val scnGetAllUsers = scenario("Get All Users").exec(getAllUsers).pause(2)
  val scnGetUserById = scenario("Get User Profile By Id").exec(getUserById).pause(2)
  val scnCreateUser = scenario("Create New User").exec(createUser).pause(2)
  val scnDeleteUser = scenario("Delete User By Id").exec(deleteUserById).pause(2)

  //setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))

  setUp(
      scnGetAllUsers.inject(rampUsers(50) over (10 seconds)),
      scnGetUserById.inject(rampUsers(50) over (10 seconds)),
      scnCreateUser.inject(rampUsers(50) over (10 seconds)),
      scnDeleteUser.inject(rampUsers(5) over (10 seconds))
  ).protocols(httpConf)

}
