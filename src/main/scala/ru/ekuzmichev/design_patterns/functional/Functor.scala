package ru.ekuzmichev.design_patterns.functional

object Functor {
  trait Functor[F[_]] {
    def map[T, Y](l: F[T])(f: T => Y): F[Y]
  }

  object Functors {
    val listFunctor: Functor[List] = new Functor[List] {
      override def map[T, Y](l: List[T])(f: (T) => Y): List[Y] = l.map(f)
    }
  }
}

object FunctorsExample {
  import Functor.Functors._
  def main(args: Array[String]): Unit = {
    val numbers = List(1, 2, 3, 4, 5, 6)
    val mapping = Map(
      1 -> "one",
      2 -> "two",
      3 -> "three",
      4 -> "four",
      5 -> "five",
      6 -> "six"
    )
    println(s"The numbers doubled are: ${listFunctor.map(numbers)(_ * 2)}")
    println(s"The numbers with strings are: ${listFunctor.map(numbers)(i => (i, mapping(i)))}")
  }
}
