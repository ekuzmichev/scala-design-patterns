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

    // IndexedSeq guarantees that getting elements by index will be efficient
    def balancedFold[T, Y](list: IndexedSeq[T], m: Monoid[Y])(f: T => Y): Y =
      if (list.isEmpty) {
        m.zero
      } else if (list.length == 1) {
        f(list(0))
      } else {
        val (left, right) = list.splitAt(list.length / 2)
        m.op(balancedFold(left, m)(f), balancedFold(right, m)(f))
      }

    def compose[T, Y](a: Monoid[T], b: Monoid[Y]): Monoid[(T, Y)] =
      new Monoid[(T, Y)] {
        val zero: (T, Y) = (a.zero, b.zero)

        override def op(l: (T, Y), r: (T, Y)): (T, Y) =
          (a.op(l._1, r._1), b.op(l._2, r._2))
      }

    def mapMerge[K, V](a: Monoid[V]): Monoid[Map[K, V]] =
      new Monoid[Map[K, V]] {
        override def zero: Map[K, V] = Map()

        override def op(l: Map[K, V], r: Map[K, V]): Map[K, V] =
          (l.keySet ++ r.keySet).foldLeft(zero) { case (res, key) =>
            res.updated(key, a.op(l.getOrElse(key, a.zero), r.getOrElse(key, a.zero)))
          }
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

object MonoidBalancedFoldExample {
  import Monoid._
  import Monoid.Monoids._

  def main(args: Array[String]): Unit = {
    val numbers = Array(1, 2, 3, 4)
    println(s"4! is: ${MonoidOperations.balancedFold(numbers, intMultiplication)(identity)}")
  }
}

object ComposedMonoidExample {
  import Monoid._
  import Monoid.Monoids._

  def main(args: Array[String]): Unit = {
    val numbers                           = Array(1, 2, 3, 4, 5, 6)
    val sumAndProduct: Monoid[(Int, Int)] = MonoidOperations.compose(intAddition, intMultiplication)
    println(s"The sum and product is: ${MonoidOperations.balancedFold(numbers, sumAndProduct)(i => (i, i))}")
  }
}

object MapMergeExample {
  import Monoid._
  import Monoid.Monoids._

  def main(args: Array[String]): Unit = {
    val features = Array("hello", "features", "for", "ml", "hello", "for", "features")

    val counterMonoid: Monoid[Map[String, Int]] = MonoidOperations.mapMerge(intAddition)

    println(s"The features are: ${MonoidOperations.balancedFold(features, counterMonoid)(i => Map(i -> 1))}")
  }
}
