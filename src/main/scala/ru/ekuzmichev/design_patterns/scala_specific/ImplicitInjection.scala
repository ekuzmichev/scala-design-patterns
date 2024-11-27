package ru.ekuzmichev.design_patterns.scala_specific

object ImplicitInjection {
  object ImplicitsDI {
    case class Person(name: String, age: Int)

    trait DatabaseService {
      def getPeople(): List[Person]
    }

    class DatabaseServiceImpl extends DatabaseService {
      override def getPeople(): List[Person] = List(
        Person("Ivan", 26),
        Person("Maria", 26),
        Person("John", 25)
      )
    }

    trait UserService {
      // A drawback of this approach is the method signatures, which can get more complex when we have more dependencies.
      def getAverageAgeOfPeople()(implicit ds: DatabaseService): Double
    }

    class UserServiceImpl extends UserService {
      override def getAverageAgeOfPeople()(implicit ds: DatabaseService): Double = {
        val (s, c) = ds.getPeople().foldLeft((0, 0)) { case ((sum, count), person) =>
          (sum + person.age, count + 1)
        }
        s.toDouble / c.toDouble
      }
    }

    object DI {
      implicit val databaseService = new DatabaseServiceImpl
      implicit val userService     = new UserServiceImpl
    }
  }
}

object ImplicitsDIExample {
  import ImplicitInjection.ImplicitsDI.DI._

  def main(args: Array[String]): Unit = {
    System.out.println(s"The average age of the people is: ${userService.getAverageAgeOfPeople()}")
  }
}
