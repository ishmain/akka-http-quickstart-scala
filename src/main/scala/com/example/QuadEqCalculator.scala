package com.example

case class QuadEqCalcCoefficients(a: Double, b: Double, c: Double)
case class QuadEqCalcSolution(roots: Set[Double])

object QuadEqCalculator {
  val emptyResultSet: Set[Double] = Set.empty

  def stringToCoefficients(coefficientsAsString: String): Option[QuadEqCalcCoefficients] = {
    try {
      val parts = coefficientsAsString.split(",")
      val a = parts(0).toDouble
      val b = parts(1).toDouble
      val c = parts(2).toDouble
      Some(QuadEqCalcCoefficients(a, b, c))
    }
    catch {
      case _: Throwable =>
        None
    }
  }

  def solveEquation(coefficientsAsString: String): QuadEqCalcSolution = {
    stringToCoefficients(coefficientsAsString) match {
      case Some(x) => solveEquation(x)
      case None => QuadEqCalcSolution(emptyResultSet)
    }
  }

  def solveEquation(coefficients: QuadEqCalcCoefficients): QuadEqCalcSolution = {
    val a = coefficients.a
    val b = coefficients.b
    val c = coefficients.c

    val roots =
      if (b * b - 4.0 * a * c >= 0 && a != 0) {
        Set(1, -1).map(sign => (-b + sign * math.sqrt(b * b - 4.0 * a * c)) / (2.0 * a))
      }
      else {
        emptyResultSet
      }

    QuadEqCalcSolution(roots)
  }
}
