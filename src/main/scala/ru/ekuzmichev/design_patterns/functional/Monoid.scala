package ru.ekuzmichev.design_patterns.functional

object Monoid {
  trait Monoid[T] {
    def op(l: T, r: T): T
    def zero: T
  }

  object Monoids {
    val intAddition: Monoid[Int] = new Monoid[Int] {
      override val zero: Int               = 0
      override def op(l: Int, r: Int): Int = l + r
    }

    val intMultiplication: Monoid[Int] = new Monoid[Int] {
      override val zero: Int               = 1
      override def op(l: Int, r: Int): Int = l * r
    }

    val stringConcatenation: Monoid[String] = new Monoid[String] {
      override val zero: String                     = ""
      override def op(l: String, r: String): String = l + r
    }
  }

  object MonoidOperations {
    def fold[T](list: List[T], m: Monoid[T]): T = list.foldLeft(m.zero)(m.op)

    def foldMap[T, Y](list: List[T], m: Monoid[Y])(f: T => Y): Y =
      list.foldLeft(m.zero) { case (t, y) =>
        m.op(t, f(y))
      }
  }
}

object MonoidFoldingExample {
  import Monoid.Monoids._

  def main(args: Array[String]): Unit = {
    val strings = List("This is\n", "a list of\n", "strings!")
    val numbers = List(1, 2, 3, 4, 5, 6)

    println(s"Left folded:\n${strings.foldLeft(stringConcatenation.zero)(stringConcatenation.op)}")
    println(s"Right folded:\n${strings.foldRight(stringConcatenation.zero)(stringConcatenation.op)}")
    println(s"6! is: ${numbers.foldLeft(intMultiplication.zero)(intMultiplication.op)}")
  }
}

object MonoidFoldingGenericExample {
  import Monoid._
  import Monoid.Monoids._

  def main(args: Array[String]): Unit = {
    val strings = List("This is\n", "a list of\n", "strings!")
    val numbers = List(1, 2, 3, 4, 5, 6)

    println(s"Left folded:\n${MonoidOperations.fold(strings, stringConcatenation)}")
    println(s"Right folded:\n${MonoidOperations.fold(strings, stringConcatenation)}")
    println(s"6! is: ${MonoidOperations.fold(numbers, intMultiplication)}")
    println(s"Fold-mapped:\n${MonoidOperations.foldMap(numbers, stringConcatenation)(n => s"$n#")}")
  }
}
