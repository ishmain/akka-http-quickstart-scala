package com.example

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

//#quadeq-case-classes
final case class QuadEqQuery(a: Double, b: Double, c: Double)
//#quadeq-case-classes

object QuadEqActor {
  // actor protocol
  sealed trait Command
  final case class Calculate(query: QuadEqQuery, replyTo: ActorRef[CalculationResponse]) extends Command
  final case class CalculateFromString(coefficients: String, replyTo: ActorRef[CalculationResponse]) extends Command

  final case class CalculationResponse(roots: Set[Double])

  def apply(): Behavior[Command] =
    Behaviors.receiveMessage {
      // Calculate from QuadEqQuery (which comes from POST)
      case Calculate(query, replyTo) =>
        val quadEqCalcCoefficients = QuadEqCalcCoefficients(query.a, query.b, query.c)
        val calcSolution = QuadEqCalculator.solveEquation(quadEqCalcCoefficients)
        val calculationResponse = CalculationResponse(calcSolution.roots)
        replyTo ! CalculationResponse(calculationResponse.roots)
        Behaviors.same

      // Calculate from string that that is a comma separated list of three doubles (coefficients)
      case CalculateFromString(coefficients, replyTo) =>
        val calcSolution = QuadEqCalculator.solveEquation(coefficients)
        val calculationResponse = CalculationResponse(calcSolution.roots)
        replyTo ! CalculationResponse(calculationResponse.roots)
        Behaviors.same
    }
}
