package ru.ekuzmichev.design_patterns.structural

import scala.collection.mutable

// The purpose of the flyweight design pattern is to minimize the memory
// usage with the help of an object that shares as much data as possible with other similar objects.
//
// There are many cases where many objects share the same information.
// A common example when talking about flyweight is word processing.
// Instead of representing each character with all the information
// about font, size, color, image, and so on, we could just store
// the positions for similar characters and have a reference to one object
// that contains the common information.
// This makes the usage of memory significantly smaller.
// Otherwise, such applications would become unusable.
//
object Flyweight {
  sealed abstract class Color
  case object Red     extends Color
  case object Green   extends Color
  case object Blue    extends Color
  case object Yellow  extends Color
  case object Magenta extends Color

  class Circle private (color: Color) {
    println(s"Creating a circle with $color color.")

    override def toString: String = s"Circle($color)"
  }

  // Circle factory
  object Circle {
    private val cache = mutable.Map.empty[Color, Circle]

    def apply(color: Color): Circle = cache.getOrElseUpdate(color, new Circle(color))

    def numOfCirclesCreated(): Int = cache.size
  }

  class Graphic {
    private val items = mutable.ListBuffer.empty[(Int, Int, Double, Circle)]

    def addCircle(x: Int, y: Int, radius: Double, circle: Circle): Unit = {
      items += ((x, y, radius, circle))
    }

    def draw(): Unit = {
      items.foreach { case (x, y, radius, circle) =>
        println(s"Drawing a circle at ($x, $y) with radius $radius: $circle")
      }
    }
  }
}

object FlyweightExample {
  import Flyweight._
  def main(args: Array[String]): Unit = {
    val graphic = new Graphic

    graphic.addCircle(1, 1, 1.0, Circle(Green))
    graphic.addCircle(1, 2, 1.0, Circle(Red))
    graphic.addCircle(2, 1, 1.0, Circle(Blue))
    graphic.addCircle(2, 2, 1.0, Circle(Green))
    graphic.addCircle(2, 3, 1.0, Circle(Yellow))
    graphic.addCircle(3, 2, 1.0, Circle(Magenta))
    graphic.addCircle(3, 3, 1.0, Circle(Blue))
    graphic.addCircle(4, 3, 1.0, Circle(Blue))
    graphic.addCircle(3, 4, 1.0, Circle(Yellow))
    graphic.addCircle(4, 4, 1.0, Circle(Red))

    graphic.draw()

    println(s"Total number of circle objects created: ${Circle.numOfCirclesCreated()}")
  }
}
