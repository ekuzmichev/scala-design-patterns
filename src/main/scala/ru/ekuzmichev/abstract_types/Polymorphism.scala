package ru.ekuzmichev.abstract_types

// Polymorphism helps us to write generic code that can be reused and applied to a variety of types.

// Subtype polymorphism
//
// It's related to overriding methods in concrete class implementations
// Subtype polymorphism is expressed using inheritance with the extends keyword.

abstract class Item {
  def pack: String
}

class Fruit extends Item {
  override def pack: String = "I'm a fruit and I'm packed in a bag."
}

class Drink extends Item {
  override def pack: String = "I'm a drink and I'm packed in a bottle."
}

object SubtypePolymorphismExample {
  def main(args: Array[String]): Unit = {
    val shoppingBasket: List[Item] = List(
      new Fruit,
      new Drink
    )
    shoppingBasket.foreach(i => System.out.println(i.pack))
  }
}

// Parametric polymorphism
//
// Generics are parametric polymorphism. They allow us to define methods or data structures over any type,
// or a subset of a given type. Concrete types can then be specified at a later stage.

// Ad hoc polymorphism
//
// Ad hoc polymorphism is similar to parametric polymorphism;
// however, in this case, the type of arguments is important,
// as the concrete implementation will depend on it.
// It is resolved at compile time, unlike subtype polymorphism,
// which is done during runtime. This is somewhat similar to function overloading.
//
// Ad hoc polymorphism allows us to extend our code without modifying the base classes.

trait AdderAdHoc[T] {
  def sum(a: T, b: T): T
}

object AdderAdHoc {
  def sum[T: AdderAdHoc](a: T, b: T): T = implicitly[AdderAdHoc[T]].sum(a, b)

  implicit val int2AdderAdHoc: AdderAdHoc[Int] = new AdderAdHoc[Int] {
    override def sum(a: Int, b: Int): Int = a + b
  }

  // same implementation as above, but allowed when the trait has a single method
  implicit val string2AdderAdHoc: AdderAdHoc[String] =
    (a: String, b: String) => s"$a concatenated with $b"
}

object AdhocPolymorphismExample {
  import AdderAdHoc._

  def main(args: Array[String]): Unit = {
    System.out.println(s"The sum of 1 + 2 is ${sum(1, 2)}")
    System.out.println(s"The sum of abc + def is '${sum("abc", "def")}''")

    {
      // Implicit implementation that supports all Numeric types as once
      implicit def numeric2Adder[T: Numeric]: AdderAdHoc[T] = new AdderAdHoc[T] {
        override def sum(a: T, b: T): T = implicitly[Numeric[T]].plus(a, b)
      }

      System.out.println(s"The sum of 1.2 + 6.5 is ${sum(1.2, 6.5)}")
    }
  }
}
