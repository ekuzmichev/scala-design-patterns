package ru.ekuzmichev.design_patterns.creational

// The prototype design pattern is a creational design pattern that involves
// creating objects by cloning them from existing ones.
// Its purpose is related to performance and keeping it high by trying to avoid expensive calls.
//
// The prototype design pattern is useful when performance is important.
// Using the copy method, we can get instances that otherwise take time to create.
// The slowness could be caused by some calculations performed during creation,
// a database call that retrieves data, and so on.
//
// The prototype design pattern should be really used in cases
// where there might be a massive performance impact without it.
object Prototype {

  /** Represents a bio cell
    */
  case class Cell(dna: String, proteins: List[String])
}

object PrototypeExample {
  import Prototype._

  def main(args: Array[String]): Unit = {
    val initialCell = Cell("abcd", List("protein1", "protein2"))
    val copy1       = initialCell.copy()
    val copy2       = initialCell.copy()
    val copy3       = initialCell.copy(dna = "1234")
    System.out.println(s"The prototype is: ${initialCell}")
    System.out.println(s"Cell 1: ${copy1}")
    System.out.println(s"Cell 2: ${copy2}")
    System.out.println(s"Cell 3: ${copy3}")
    System.out.println(s"1 and 2 are equal: ${copy1 == copy2}")
  }
}
