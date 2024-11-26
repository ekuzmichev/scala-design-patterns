package ru.ekuzmichev.design_patterns.behavioral

import java.util.StringTokenizer
import scala.collection.mutable
import scala.jdk.CollectionConverters.EnumerationHasAsScala

// The interpreter design pattern is useful for specifying how
// to evaluate sentences in a language by representing it using
// classes and building syntax trees to evaluate the language expressions.
//
object Interpreter {
  // Reverse Polish notation syntax interpretation example
  //
  // '1 2 + 3 * 9 10 + -' = -10
  //
  // Two main types of expressions
  // - Terminal expression:
  //    This is the Number class.
  //    It is a terminal in the sense that when building the
  //    syntax tree of an expression, it has no other children (leaf node).
  // - Non-terminal expression:
  //    These are the Add, Subtract, and Multiply classes.
  //    They have children expressions and this is how the entire syntax tree is built.

  trait Expression {
    def interpret(): Int
  }

  class Number(n: Int) extends Expression {
    override def interpret(): Int = n
  }

  // the right-hand expression first and then the left-hand one
  class Add(right: Expression, left: Expression) extends Expression {
    override def interpret(): Int = left.interpret() + right.interpret()
  }

  class Subtract(right: Expression, left: Expression) extends Expression {
    override def interpret(): Int = left.interpret() - right.interpret()
  }

  class Multiply(right: Expression, left: Expression) extends Expression {
    override def interpret(): Int = left.interpret() * right.interpret()
  }

  // Factory & by-name parameters
  object Expression {
    def apply(operator: String, left: => Expression, right: => Expression): Option[Expression] =
      operator match {
        case "+"                    => Some(new Add(right, left))
        case "-"                    => Some(new Subtract(right, left))
        case "*"                    => Some(new Multiply(right, left))
        case i if i.matches("\\d+") => Some(new Number(i.toInt))
        case _                      => None
      }
  }

  class RPNParser {
    def parse(expression: String): Expression = {
      val tokenizer = new StringTokenizer(expression)
      tokenizer.asScala
        .foldLeft(mutable.Stack[Expression]()) { case (result, token) =>
          val item = Expression(token.toString, result.pop(), result.pop())
          item.foreach(result.push)
          result
        }
        .pop()
    }
  }

  class RPNInterpreter {
    def interpret(expression: Expression): Int = expression.interpret()
  }
}

object InterpreterExample {
  import Interpreter._

  def main(args: Array[String]): Unit = {
    val expr1       = "1 2 + 3 * 9 10 + -" // (1 + 2) * 3 - (9 + 10) = -10
    val expr2       = "1 2 3 4 5 * * - +"  // 1 + 2 - 3 * 4 * 5 = -57
    val expr3       = "12 -"               // invalid
    val parser      = new RPNParser
    val interpreter = new RPNInterpreter

    println(s"The result of '$expr1' is: ${interpreter.interpret(parser.parse(expr1))}")
    println(s"The result of '$expr2' is: ${interpreter.interpret(parser.parse(expr2))}")

    try {
      println(s"The result is: ${interpreter.interpret(parser.parse(expr3))}")
    } catch {
      case _: Throwable => println(s"'$expr3' is invalid.")
    }
  }
}
