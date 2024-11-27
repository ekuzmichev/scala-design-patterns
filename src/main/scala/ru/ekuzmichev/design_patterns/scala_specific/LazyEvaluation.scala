package ru.ekuzmichev.design_patterns.scala_specific

// Lazy evaluation makes sure that an expression is evaluated only once when it is actually needed.
// Scala supports lazy evaluation in a couple of flavors—lazy variables and by-name parameters
// The lazy variables will be calculated only once, whereas
// the by-name parameters will be calculated every time they are referred to in a method
//
object LazyEvaluation {
  object EvaluateByNameParamsOnlyOnce {
    case class Person(name: String, age: Int)

    object Person {
      def getFromDatabase(): List[Person] = {
        // simulate we're getting people from database by sleeping
        println("Retrieving people...")

        Thread.sleep(3000)

        List(
          Person("Ivan", 26),
          Person("Maria", 26),
          Person("John", 25)
        )
      }
    }

    def printPeopleBad(people: => List[Person]): Unit = {
      println(s"Print first time: $people")
      println(s"Print second time: $people")
    }

    def printPeopleGood(people: => List[Person]): Unit = {
      lazy val peopleCopy = people
      println(s"Print first time: $peopleCopy")
      println(s"Print second time: $peopleCopy")
    }
  }
}

object EvaluateByNameParamsOnlyOnceExample {

  import LazyEvaluation.EvaluateByNameParamsOnlyOnce._
  import LazyEvaluation.EvaluateByNameParamsOnlyOnce.Person._

  def main(args: Array[String]): Unit = {
    println("Now printing bad.")
    printPeopleBad(getFromDatabase())
    println("Now printing good.")
    printPeopleGood(getFromDatabase())
  }
}
