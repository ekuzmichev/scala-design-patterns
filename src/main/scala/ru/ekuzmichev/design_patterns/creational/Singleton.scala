package ru.ekuzmichev.design_patterns.creational

// The singleton design pattern ensures that a class has only one object instance in the entire application.
// It introduces a global state in the applications it is used in.
//
// A singleton object can be initialized using different strategies—lazy initialization or eager initialization.
// This all depends on the intended use, the time it takes an object to be initialized, and so on.

object Singleton {
  object StringUtils {
    def countNumberOfSpaces(text: String): Int = text.split("\\s+").length - 1
  }
}

object SingletonExample {
  import Singleton._

  def main(args: Array[String]): Unit = {
    val sentence = "Hello there! I am a utils example."
    System.out.println(
      s"The number of spaces in '$sentence' is: ${StringUtils.countNumberOfSpaces(sentence)}"
    )
  }
}