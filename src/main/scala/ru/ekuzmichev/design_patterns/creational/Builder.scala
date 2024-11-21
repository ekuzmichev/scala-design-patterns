package ru.ekuzmichev.design_patterns.creational

// The builder design pattern helps to create instances of classes using class methods
// rather than the class constructors.
// It is particularly useful in cases where a class might need multiple versions
// of its constructor in order to allow different usage scenarios.
//
// Moreover, in some cases, it might not even be possible to define all combinations
// or they might not be known. The builder design pattern uses an extra object,
// called builder, in order to receive and store initialization parameters before
// building the final version of an object.
//
//There are actually three main ways in which we can represent the builder design pattern in Scala:
//
//- The classical way like other object-oriented languages.
//    This way is actually not recommended, even though it is possible in Scala.
//    It uses mutability in order to work, which contradicts the immutability principle
//    of the language. We will show it here for completeness and in order to point out
//    how much easier it is to achieve the builder design pattern using simple features of Scala.
//- Using case classes with default parameters
//- Using generalized type constraints

object Builder {
  // It is not recommended
  object JavaLikeImplementation {
    class Person(builder: PersonBuilder) {
      val firstName = builder.firstName
      val lastName  = builder.lastName
      val age       = builder.age
    }

    class PersonBuilder {
      var firstName = ""
      var lastName  = ""
      var age       = 0

      def setFirstName(firstName: String): PersonBuilder = {
        this.firstName = firstName
        this
      }

      def setLastName(lastName: String): PersonBuilder = {
        this.lastName = lastName
        this
      }
      def setAge(age: Int): PersonBuilder = {
        this.age = age
        this
      }

      def build(): Person = new Person(this)
    }
  }

  object CaseClassImplementation {
    case class Person(
        firstName: String = "",
        lastName: String = "",
        age: Int = 0
    )

  }

  // Type-safe builder
  //
  // It is also not recommended due to mutability
  //
  // Drawbacks:
  // - Complexity
  // - Mutability
  // - A predefined order of initialization
  object GeneralizedTypeConstraintsImplementation {
    sealed trait BuildStep
    sealed trait HasFirstName extends BuildStep
    sealed trait HasLastName  extends BuildStep

    class Person(
        val firstName: String,
        val lastName: String,
        val age: Int
    )

    class PersonBuilder[PassedStep <: BuildStep] private (
        var firstName: String,
        var lastName: String,
        var age: Int
    ) {

      protected def this() = this("", "", 0)

      protected def this(pb: PersonBuilder[_]) = this(
        pb.firstName,
        pb.lastName,
        pb.age
      )

      def setFirstName(firstName: String): PersonBuilder[HasFirstName] = {
        this.firstName = firstName
        new PersonBuilder[HasFirstName](this)
      }

      def setLastName(lastName: String)(implicit ev: PassedStep =:= HasFirstName): PersonBuilder[HasLastName] = {
        this.lastName = lastName
        new PersonBuilder[HasLastName](this)
      }

      def setAge(age: Int): PersonBuilder[PassedStep] = {
        this.age = age
        this
      }

      def build()(implicit ev: PassedStep =:= HasLastName): Person =
        new Person(
          firstName,
          lastName,
          age
        )
    }

    object PersonBuilder {
      def apply() = new PersonBuilder[BuildStep]()
    }
  }

  object RequireStatementsImplementation {
    case class Person(
        firstName: String = "",
        lastName: String = "",
        age: Int = 0
    ) {
      require(firstName != "", "First name is required.")
      require(lastName != "", "Last name is required.")
    }
  }
}

object JavaLikeImplementationExample {
  import Builder.JavaLikeImplementation._

  def main(args: Array[String]): Unit = {
    val person: Person = new PersonBuilder()
      .setFirstName("Bob")
      .setLastName("Marley")
      .setAge(44)
      .build()

    System.out.println(s"Person: ${person.firstName} ${person.lastName}. Age: ${person.age}.")
  }
}

object CaseClassImplementationExample {
  import Builder.CaseClassImplementation._

  def main(args: Array[String]): Unit = {
    val person1 = Person(
      firstName = "Bob",
      lastName = "Marley",
      age = 44
    )
    val person2 = Person(
      firstName = "Fritz"
    )
    System.out.println(s"Person 1: ${person1}")
    System.out.println(s"Person 2: ${person2}")
  }
}

object GeneralizedTypeConstraintsImplementationExample {
  import Builder.GeneralizedTypeConstraintsImplementation._

  def main(args: Array[String]): Unit = {
    val person = PersonBuilder()
      .setFirstName("Bob")
      .setLastName("Marley")
      .setAge(44)
      .build()
    System.out.println(s"Person: ${person.firstName} ${person.lastName}. Age: ${person.age}.")
  }
}

object RequireStatementsImplementationExample {
  import Builder.RequireStatementsImplementation._

  def main(args: Array[String]): Unit = {
    val person1 = Person(
      firstName = "Bob",
      lastName = "Marley",
      age = 26
    )

    System.out.println(s"Person 1: ${person1}")

    try {
      val person2 = Person(
        firstName = "John"
      )

      System.out.println(s"Person 2: ${person2}")
    } catch {
      case e: Throwable =>
        e.printStackTrace()
    }
  }
}
