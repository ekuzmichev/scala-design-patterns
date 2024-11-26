package ru.ekuzmichev.design_patterns.behavioral

// Value objects are small and simple immutable objects.
// Their equality is based not on identity, but on value equality.
//
// Value objects are used to represent numbers, money, dates, and so on.
// They should be small and immutable; otherwise, changing values could cause
// bugs and unexpected behavior. They are quite useful in multithreaded applications
// due to their immutability. They are also commonly used as data transfer
// objects in enterprise applications.
//
object ValueObject {
  case class Date(
      day: Int,
      month: String,
      year: Int
  )
}

object ValueObjectExample {
  import ValueObject._

  def main(args: Array[String]): Unit = {
    val thirdOfMarch = Date(3, "MARCH", 2016)
    val fourthOfJuly = Date(4, "JULY", 2016)
    val newYear1     = Date(31, "DECEMBER", 2015)
    val newYear2     = Date(31, "DECEMBER", 2015)

    println(s"The 3rd of March 2016 is the same as the 4th of July 2016: ${thirdOfMarch == fourthOfJuly}")
    println(s"The new year of 2015 is here twice: ${newYear1 == newYear2}")
  }
}
