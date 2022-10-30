package com.example

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class QuadEqSpec extends AnyWordSpec with Matchers with ScalaFutures with ScalatestRouteTest {
  lazy val testKit: ActorTestKit = ActorTestKit()
  implicit def typedSystem: ActorSystem[Nothing] = testKit.system
  override def createActorSystem(): akka.actor.ActorSystem =
    testKit.system.classicSystem

  // Here we need to implement all the abstract members of QuadEqRoutes.
  // We use the real QuadEqActor to test it while we hit the Routes,
  // but we could "mock" it by implementing it in-place or by using a TestProbe
  // created with testKit.createTestProbe()
  val quadEqActor: ActorRef[QuadEqActor.Command] = testKit.spawn(QuadEqActor())
  lazy val routes: Route = new QuadEqRoutes(quadEqActor).routes

  // use the json formats to marshal and unmarshall objects in the test
  import QuadEqJsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  //#set-up

  "QuadEqRoutes" should {

    "return the usage text (GET /quadeq)" in {
      // note that there's no need for the host part in the uri:
      val request = HttpRequest(uri = "/quadeq")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be plain UTF-8 text:
        contentType should ===(ContentTypes.`text/plain(UTF-8)`)

        // and usage text in the output:
        entityAs[String] should include regex("USAGE:")
      }
    }

    "return Quadratic Equation solution (GET /quadeq/<coefficients>)" in {
      val request = HttpRequest(uri = "/quadeq/1,-7,10")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and quadratic equation solution with two roots:
        entityAs[String] should ===("""{"roots":[5.0,2.0]}""")
      }
    }

    "return no roots for incorrect input string (GET /quadeq/<incorrect input>)" in {
      val request = HttpRequest(uri = "/quadeq/some_string_that_does_not_represent_three_doubles_separated_by_comma")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and no equation solution:
        entityAs[String] should ===("""{"roots":[]}""")
      }
    }

    "be able to find solution for (POST /quaeq)" in {
      val quadEqQuery = QuadEqQuery(1, -7, 10)
      val quadEqEntity = Marshal(quadEqQuery).to[MessageEntity].futureValue // futureValue is from ScalaFutures

      // using the RequestBuilding DSL:
      val request = Post("/quadeq").withEntity(quadEqEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        contentType should ===(ContentTypes.`application/json`) // we expect the response to be json:

        // and quadratic equation solution with two roots:
        entityAs[String] should ===("""{"roots":[5.0,2.0]}""")
      }
    }
  }
}
