package ru.ekuzmichev.design_patterns.scala_specific

// There are functions that are not defined for all possible inputs.
// A simple example is the square root function—it will only work for real numbers if they are non-negative
// They are quite useful, as we can basically perform filter and map at once.
// This means fewer CPU cycles and more readable code.
//
// Partial functions are not partially applied functions
//
object PartialFunctions {
  val squareRoot: PartialFunction[Int, Double] = {
    case a if a >= 0 => Math.sqrt(a)
  }

  // Squares negative numbers
  val square: PartialFunction[Int, Double] = {
    case a if a < 0 => Math.pow(a, 2)
  }
}

object PartialFunctionsExample {
  import PartialFunctions._

  def main(args: Array[String]): Unit = {
    val items = List(-1, 10, 11, -36, 36, -49, 49, 81)
    println(s"Can we calculate a root for -10: ${squareRoot.isDefinedAt(-10)}")
    println(s"Square roots: ${items.collect(squareRoot)}")
    println(s"Square roots or squares: ${items.collect(squareRoot.orElse(square))}")
  }
}
