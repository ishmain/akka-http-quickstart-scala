package com.example

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.AskPattern._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.example.QuadEqActor._

import scala.concurrent.Future


class QuadEqRoutes(actor: ActorRef[QuadEqActor.Command])(implicit val system: ActorSystem[_]) {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import QuadEqJsonFormats._

  private implicit val timeout: Timeout =
    Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def calculate(query: QuadEqQuery): Future[CalculationResponse] = {
    actor.ask(QuadEqActor.Calculate(query, _))
  }
  def calculateFromString(query: String): Future[CalculationResponse] = {
    actor.ask(QuadEqActor.CalculateFromString(query, _))
  }

  val routes: Route =
    pathPrefix("quadeq") {
      concat(
        pathEnd {
          concat(
            get {
              val helpText =
                """
                  |This is an Akka-HTTP test application for Quadratic Equation
                  |
                  |USAGE:
                  |
                  |Example 1 (GET):
                  |    Command: curl localhost:8080/quadeq/1,-6,-7
                  |    Expected result: {"roots":[7.0,-1.0]}
                  |
                  |Example 2 (GET):
                  |    Command: curl localhost:8080/quadeq/some_string_that_cannot_be_parsed
                  |    Expected result: {"roots":[]}
                  |
                  |Example 3 (POST):
                  |    Command: curl -H "Content-type: application/json" -X POST http://localhost:8080/quadeq -d "{\"a\": 1, \"b\": -7, \"c\": 10}"
                  |    expected result: {"roots":[5.0,2.0]}
                  |
                  |""".stripMargin
              complete(helpText)
            },
            post {
              // curl -H "Content-type: application/json" -X POST http://localhost:8080/quadeq -d "{\"a\": 1, \"b\": -7, \"c\": 10}"
              entity(as[QuadEqQuery]) { query =>
                onSuccess(calculate(query)) { response =>
                  complete(response)
                }
              }
            }
          )
        },
        path(Segment) { coefficientsAsString =>
          concat(
            get {
              // curl localhost:8080/quadeq/1,-7,10
              onSuccess(calculateFromString(coefficientsAsString)) { response =>
                complete(response)
              }
            }
          )
        }
      )
    }
}
