package ru.ekuzmichev.design_patterns.scala_specific

// The stackable traits design pattern is based on mixin composition
//
// There is already an example in Decorator.StackableTrait
//
// Stackable traits are always executed from the right mixin to the left.
// Sometimes, however, if we only get output, and it doesn't depend on what is passed to the method,
// we simply end up with method calls on a stack, which then get evaluated, and it will appear
// as if things are applied from left to right.
//
object StackableTraits {

  abstract class StringWriter {
    def write(data: String): String
  }

  class BasicStringWriter extends StringWriter {
    override def write(data: String): String = s"Writing the following data: $data"
  }

  trait CapitalizingStringWriter extends StringWriter {
    abstract override def write(data: String): String = {
      super.write(data.split("\\s+").map(_.capitalize).mkString(" "))
    }
  }

  trait UppercasingStringWriter extends StringWriter {
    abstract override def write(data: String): String = {
      super.write(data.toUpperCase)
    }
  }

  trait LowercasingStringWriter extends StringWriter {
    abstract override def write(data: String): String = {
      super.write(data.toLowerCase)
    }
  }
}

object StackableTraitsExample {
  import StackableTraits._

  def main(args: Array[String]): Unit = {
    val writer1 = new BasicStringWriter with UppercasingStringWriter with CapitalizingStringWriter

    val writer2 = new BasicStringWriter with CapitalizingStringWriter with LowercasingStringWriter

    val writer3 = new BasicStringWriter
      with CapitalizingStringWriter
      with UppercasingStringWriter
      with LowercasingStringWriter

    val writer4 = new BasicStringWriter
      with LowercasingStringWriter
      with CapitalizingStringWriter
      with UppercasingStringWriter

    val testText = "we like learning scala!"

    println(s"Writer 1: '${writer1.write(testText)}'")

    println(s"Writer 2: '${writer2.write(testText)}'")

    println(s"Writer 3: '${writer3.write(testText)}'")

    println(s"Writer 4: '${writer4.write(testText)}'")
  }
}
