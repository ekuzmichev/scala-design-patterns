package ru.ekuzmichev.design_patterns.functional

import java.io.{File, PrintWriter}
import scala.io.Source

object Monad {
  object CustomOption {
    trait Functor[T] {
      def map[Y](f: T => Y): Functor[Y]
    }

    trait Monad[T] extends Functor[T] {
      def unit[Y](value: Y): Monad[Y]

      def flatMap[Y](f: T => Monad[Y]): Monad[Y]

      override def map[Y](f: T => Y): Monad[Y] =
        flatMap(i => unit(f(i)))
    }

    sealed trait Option[A] extends Monad[A]

    case class Some[A](a: A) extends Option[A] {
      override def unit[Y](value: Y): Monad[Y] = Some(value)

      override def flatMap[Y](f: A => Monad[Y]): Monad[Y] = f(a)
    }

    case class None[A]() extends Option[A] {
      override def unit[Y](value: Y): Monad[Y] = None()

      override def flatMap[Y](f: A => Monad[Y]): Monad[Y] = None()
    }

    case class Doer() {
      def getAlgorithm(isFail: Boolean): Option[Algorithm] =
        if (isFail) None() else Some(Algorithm())
    }

    case class Algorithm() {
      def getImplementation(isFail: Boolean, left: Int, right: Int): Option[Implementation] =
        if (isFail) None() else Some(Implementation(left, right))
    }

    case class Implementation(left: Int, right: Int) {
      def compute: Int = left + right
    }
  }

  object IO {
    sealed trait State {
      def next: State
    }

    abstract class FileIO {

      // this makes sure nobody can create a state
      private class FileIOState(id: Int) extends State {
        override def next: State = new FileIOState(id + 1)
      }

      def run(args: Array[String]): Unit = {
        val action = runIO(args(0), args(1))
        action(new FileIOState(0))
      }

      def runIO(readPath: String, writePath: String): IOAction[_]
    }

    sealed abstract class IOAction[T] extends (State => (State, T)) {
      // START: we don't have to extend. We could also do this...
      def unit[Y](value: Y): IOAction[Y] = IOAction.unit(value)

      def flatMap[Y](f: T => IOAction[Y]): IOAction[Y] = {
        val self = this
        new IOAction[Y] {
          override def apply(state: State): (State, Y) = {
            val (state2, res) = self(state)
            val action2       = f(res)
            action2(state2)
          }

        }
      }

      def map[Y](f: T => Y): IOAction[Y] =
        flatMap(i => unit(f(i)))

      // END: we don't have to extend. We could also do this...
    }

    object IOAction {
      def unit[T](value: T): IOAction[T] = new EmptyAction[T](value)

      def apply[T](result: => T): IOAction[T] =
        new SimpleAction[T](result)

      private class EmptyAction[T](value: T) extends IOAction[T] {
        override def apply(state: State): (State, T) =
          (state, value)
      }

      private class SimpleAction[T](result: => T) extends IOAction[T] {
        override def apply(state: State): (State, T) =
          (state.next, result)
      }
    }

    object IOActions {
      def readFile(path: String) =
        IOAction(Source.fromFile(path).getLines())

      def writeFile(path: String, lines: Iterator[String]) =
        IOAction({
          val file = new File(path)
          printToFile(file) { p => lines.foreach(p.println) }
        })

      private def printToFile(file: File)(writeOp: PrintWriter => Unit): Unit = {
        val writer = new PrintWriter(file)
        try {
          writeOp(writer)
        } finally {
          writer.close()
        }
      }
    }
  }
}

object ForComprehensionWithListsExample {
  def main(args: Array[String]): Unit = {
    val l1 = List(1, 2, 3, 4)
    val l2 = List(5, 6, 7, 8)
    val result = for {
      x <- l1
      y <- l2
    } yield x * y
    // same as
    // val result = l1.flatMap(i => l2.map(_ * i))
    println(s"The result is: $result")
  }
}

object ForComprehensionWithObjectsExample {
  case class ListWrapper(list: List[Int]) {
    // just wrap
    def map[B](f: Int => B): List[B] = list.map(f)

    // just wrap
    def flatMap[B](f: Int => IterableOnce[B]): List[B] =
      list.flatMap(f)
  }

  def main(args: Array[String]): Unit = {
    val wrapper1 = ListWrapper(List(1, 2, 3, 4))
    val wrapper2 = ListWrapper(List(5, 6, 7, 8))
    val result = for {
      x <- wrapper1
      y <- wrapper2
    } yield x * y
    println(s"The result is: $result")
  }
}

object MonadCustomOptionExample {
  import Monad.CustomOption._

  def main(args: Array[String]): Unit = {
    println(s"The result is: ${compute(Some(Doer()), 10, 16)}")
  }

  def compute(maybeDoer: Option[Doer], left: Int, right: Int) =
    for {
      doer           <- maybeDoer
      algorithm      <- doer.getAlgorithm(isFail = false)
      implementation <- algorithm.getImplementation(isFail = false, left, right)
    } yield implementation.compute

  // OR THIS WAY:
  //  maybeDoer.flatMap {
  //    doer =>
  //      doer.getAlgorithm(false).flatMap {
  //        algorithm =>
  //          algorithm.getImplementation(false, left, right).map {
  //            implementation => implementation.compute
  //          }
  //      }
  //  }
}

object FileIOExample extends Monad.IO.FileIO {
  import Monad.IO.IOAction
  import Monad.IO.IOActions._

  def main(args: Array[String]): Unit = {
    run(args)
  }

  override def runIO(readPath: String, writePath: String): IOAction[_] =
    for {
      lines <- readFile(readPath)
      _     <- writeFile(writePath, lines.map(_.toUpperCase))
    } yield ()
}
