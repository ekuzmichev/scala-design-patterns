package ru.ekuzmichev.design_patterns.behavioral

import scala.Console.err
import scala.io.Source

// The purpose of the chain of responsibility design pattern is to decouple
// the sender of a request from its receiver by giving multiple objects
// the chance to handle the request.
//
// The original pattern is that whenever a request reaches an object
// that can process it, it doesn't go any further
// However, in some cases, we might need to push the request further
// or even multiply it and broadcast to other receivers.
//
// The chain of responsibility design pattern should be used when we want
// to decouple a sender of a request from the receivers and
// have these receivers separated into their own entities.
// It is good for creating pipelines and handling events.
//
object ChainOfResponsibility {
  // The same result could be obtained with 'abstract override def'-like stackable traits (as in Decorator pattern)
  object Classic {
    case class Money(amount: Int)

    trait Dispenser {
      val amount: Int
      val next: Option[Dispenser]

      def dispense(money: Money): Unit = {
        if (money.amount >= amount) {
          val notes = money.amount / amount
          val left  = money.amount % amount

          println(s"Dispensing $notes note/s of $amount.")

          // passes the responsibility to the next dispenser
          if (left > 0) next.map(_.dispense(Money(left)))
        } else {
          next.foreach(_.dispense(money))
        }
      }
    }

    class Dispenser50(val next: Option[Dispenser]) extends Dispenser {
      override val amount = 50
    }

    class Dispenser20(val next: Option[Dispenser]) extends Dispenser {
      override val amount: Int = 20
    }

    class Dispenser10(val next: Option[Dispenser]) extends Dispenser {
      override val amount: Int = 10
    }

    class Dispenser5(val next: Option[Dispenser]) extends Dispenser {
      override val amount: Int = 5
    }

    class ATM {
      private val dispenser: Dispenser = {
        val d1 = new Dispenser5(None)
        val d2 = new Dispenser10(Some(d1))
        val d3 = new Dispenser20(Some(d2))
        new Dispenser50(Some(d3))
      }

      def requestMoney(money: Money): Unit = {
        if (money.amount % 5 != 0) {
          err.println("The smallest nominal is 5 and we cannot satisfy your request.")
        } else {
          dispenser.dispense(money)
        }
      }
    }
  }

  // Using partial functions
  object ScalaWay {
    case class Money(amount: Int)

    trait Dispenser {
      def dispense(dispenserAmount: Int): PartialFunction[Money, Money] = {
        case Money(amount) if amount >= dispenserAmount =>
          val notes = amount / dispenserAmount
          val left  = amount % dispenserAmount

          println(s"Dispensing $notes note/s of $dispenserAmount.")

          Money(left)
        case money: Money => money
      }
    }

    class ATM extends Dispenser {
      private val dispenser: PartialFunction[Money, Money] =
        dispense(50)
          .andThen(dispense(20))
          .andThen(dispense(10))
          .andThen(dispense(5))

      def requestMoney(money: Money): Unit = {
        if (money.amount % 5 != 0) {
          err.println("The smallest nominal is 5 and we cannot satisfy your request.")
        } else {
          dispenser(money)
        }
      }
    }
  }
}

object ChainOfResponsibilityClassicExample {
  import ChainOfResponsibility.Classic._

  def main(args: Array[String]): Unit = {
    val atm = new ATM

    printHelp()

    Source.stdin.getLines().foreach(line => processLine(line, atm))
  }

  private def printHelp(): Unit = {
    println("Usage: ")
    println("1. Write an amount to withdraw...")
    println("2. Write EXIT to quit the application.")
  }

  private def processLine(line: String, atm: ATM): Unit = {
    line match {
      case "EXIT" =>
        println("Bye!")
        System.exit(0)
      case l =>
        try {
          atm.requestMoney(Money(l.toInt))
          println("Thanks!")
        } catch {
          case _: Throwable =>
            err.println(s"Invalid input: $l.")
            printHelp()
        }
    }
  }
}

object ChainOfResponsibilityScalaWayExample {
  import ChainOfResponsibility.ScalaWay._

  def main(args: Array[String]): Unit = {
    val atm = new ATM

    printHelp()

    Source.stdin.getLines().foreach(line => processLine(line, atm))
  }

  private def printHelp(): Unit = {
    println("Usage: ")
    println("1. Write an amount to withdraw...")
    println("2. Write EXIT to quit the application.")
  }

  private def processLine(line: String, atm: ATM): Unit = {
    line match {
      case "EXIT" =>
        println("Bye!")
        System.exit(0)
      case l =>
        try {
          atm.requestMoney(Money(l.toInt))
          println("Thanks!")
        } catch {
          case _: Throwable =>
            err.println(s"Invalid input: $l.")
            printHelp()
        }
    }
  }
}

