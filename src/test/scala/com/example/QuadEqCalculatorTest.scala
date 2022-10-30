package com.example

import org.scalatest.funsuite.AnyFunSuite

class QuadEqCalculatorTest extends AnyFunSuite {
  val noRoots: QuadEqCalcSolution = QuadEqCalcSolution(Set.empty)

  test("Empty input string gives no roots") {
    val coefficientsAsString = ""
    assert(QuadEqCalculator.solveEquation(coefficientsAsString) == noRoots)
  }

  test("Bad string input gives no roots") {
    val coefficientsAsString = "som bad string that cannot be converted to a set of three Doubles"
    val roots = QuadEqCalculator.solveEquation(coefficientsAsString)
    assert(QuadEqCalculator.solveEquation(coefficientsAsString) == noRoots)
  }

  test("Calc gives 5 and 2 for 1, -7, 10") {
    val coefficientsAsString = "1,-7,10"
    assert(QuadEqCalculator.solveEquation(coefficientsAsString) == QuadEqCalcSolution(Set(5.0, 2.0)))
  }

  test("Calc gives 5 and 2 for QuadEqCalcCoefficients(1, -7, 10)") {
    val quadCoefficientsAsCaseClass = QuadEqCalcCoefficients(1,-7,10)
    assert(QuadEqCalculator.solveEquation(quadCoefficientsAsCaseClass) == QuadEqCalcSolution(Set(5.0, 2.0)))
  }

  test("Calc gives 7 and 1 for -1, -6, -7") {
    val coefficientsAsString = "1,-6,-7"
    assert(QuadEqCalculator.solveEquation(coefficientsAsString) == QuadEqCalcSolution(Set(7.0, -1.0)))
  }

  test("Calc gives no roots for 0, 3, 7") {
    val coefficientsAsString = "0,3,7"
    val roots = QuadEqCalculator.solveEquation(coefficientsAsString)
    assert(QuadEqCalculator.solveEquation(coefficientsAsString) == noRoots)
  }

}
