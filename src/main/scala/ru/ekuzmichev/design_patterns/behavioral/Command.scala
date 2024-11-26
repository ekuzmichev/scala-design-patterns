package ru.ekuzmichev.design_patterns.behavioral

import scala.collection.mutable.ListBuffer

// The object that will execute commands is called invoker
//
// The purpose of the command design pattern is to encapsulate
// the information needed to perform an action at a later stage and
// pass this information to the object that will be running the actual code.
//
// The command design pattern is useful for many things, some of which include
// supporting undo actions, implementing parallel processing, or simply optimizing
// code by deferring, and possibly avoiding code execution.
//
// - Command: We can think of this as the interface and its implementations that are being called by the invoker.
// - Receiver: This is the object that actually knows how commands are executed.
//    Think of this as an object that is being passed to the command and then used in the interface method.
// - Invoker: It invokes the commands by calling their interface method.
//    It might not even know what commands are being invoked.
// - Client: It more or less guides which commands are executed when by using the invoker.
//
// The command design pattern is useful for cases where we want to delay, log, or sequence method calls
// for one reason or another.
// Another advantage is that it decouples the invoker from the object that actually performs the specific operations.
// This allows us to have modifications and to add new functionality pretty easily.
//
object Command {
  object Classic {
    case class Robot() {
      def cleanUp(): Unit = println("Cleaning up.")

      def pourJuice(): Unit = println("Pouring juice.")

      def makeSandwich(): Unit = println("Making a sandwich.")
    }

    trait RobotCommand {
      def execute(): Unit
    }

    case class MakeSandwichCommand(robot: Robot) extends RobotCommand {
      override def execute(): Unit = robot.makeSandwich()
    }

    case class PourJuiceCommand(robot: Robot) extends RobotCommand {
      override def execute(): Unit = robot.pourJuice()
    }

    case class CleanUpCommand(robot: Robot) extends RobotCommand {
      override def execute(): Unit = robot.cleanUp()
    }

    class RobotController {
      private val history = ListBuffer[RobotCommand]()

      def issueCommand(command: RobotCommand): Unit = {
        command +=: history
        command.execute()
      }

      def showHistory(): Unit =
        history.foreach(println)
    }
  }

  // It is based on by-name parameters feature of the language
  // It is replaceable with passing functions as parameters but more verbose
  object ScalaWay {
    case class Robot() {
      def cleanUp(): Unit = println("Cleaning up.")

      def pourJuice(): Unit = println("Pouring juice.")

      def makeSandwich(): Unit = println("Making a sandwich.")
    }

    trait RobotCommand {
      def execute(): Unit
    }

    case class MakeSandwichCommand(robot: Robot) extends RobotCommand {
      override def execute(): Unit = robot.makeSandwich()
    }

    case class PourJuiceCommand(robot: Robot) extends RobotCommand {
      override def execute(): Unit = robot.pourJuice()
    }

    case class CleanUpCommand(robot: Robot) extends RobotCommand {
      override def execute(): Unit = robot.cleanUp()
    }

    class RobotController {
      private val history = ListBuffer[() => Unit]()

      def issueCommand(command: => Unit): Unit = {
        command _ +=: history
        command
      }

      def showHistory(): Unit = {
        history.foreach(println)
      }
    }
  }
}

object CommandClassicExample {
  import Command.Classic._

  def main(args: Array[String]): Unit = {
    val robot           = Robot()
    val robotController = new RobotController

    robotController.issueCommand(MakeSandwichCommand(robot))

    robotController.issueCommand(PourJuiceCommand(robot))

    println("I'm eating and having some juice.")

    robotController.issueCommand(CleanUpCommand(robot))

    println("Here is what I asked my robot to do:")

    robotController.showHistory()
  }
}

object CommandScalaWayExample {
  import Command.ScalaWay._

  def main(args: Array[String]): Unit = {
    val robot           = Robot()
    val robotController = new RobotController

    robotController.issueCommand(MakeSandwichCommand(robot).execute())

    robotController.issueCommand(PourJuiceCommand(robot).execute())

    println("I'm eating and having some juice.")

    robotController.issueCommand(CleanUpCommand(robot).execute())

    println("Here is what I asked my robot to do:")

    robotController.showHistory()
  }
}
