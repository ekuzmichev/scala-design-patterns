package ru.ekuzmichev.design_patterns.scala_specific

// The fact that we cannot really modify the original library code means that we have to do something different
// Pimp my library achieves something similar to Decorator and Adapter,
// but it does this in the Scala way and some of the extra work is given to the compiler to deal with
//
object PimpMyLibrary {
  implicit class StringExtensions(val s: String) extends AnyVal {
    def isAllUpperCase: Boolean =
      !(0 until s.length).exists(index => s.charAt(index).isLower)
  }
}

object PimpMyLibraryExample {
  import PimpMyLibrary._

  def main(args: Array[String]): Unit = {
    println(s"Is 'test' all upper case: ${"test".isAllUpperCase}")
    println(s"Is 'Tes' all upper case: ${"Test".isAllUpperCase}")
    println(s"Is 'TESt' all upper case: ${"TESt".isAllUpperCase}")
    println(s"Is 'TEST' all upper case: ${"TEST".isAllUpperCase}")
  }
}
