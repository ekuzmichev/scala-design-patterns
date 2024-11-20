package ru.ekuzmichev.design_patterns.creational

// The static factory could be represented as a static method,
// which is a part of the base class.
// It is called to create concrete instances, which extend the base class.
// One of the biggest drawbacks here, however, is that if another extension
// of the base class is added, the base class (because of the static method) also has to be edited.

object StaticFactory {
  trait Animal
  case object Bird   extends Animal
  case object Mammal extends Animal
  case object Fish   extends Animal

  object Animal {
    def apply(animal: String): Animal = animal.toLowerCase match {
      case "bird"    => Bird
      case "mammal"  => Mammal
      case "fish"    => Fish
      case x: String => throw new RuntimeException(s"Unknown animal: $x")
    }
  }
}

object StaticFactoryExample {
  import StaticFactory._

  def main(args: Array[String]): Unit = {
    println("Created: " + Animal("mammal"))
  }
}
