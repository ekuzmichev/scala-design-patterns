package ru.ekuzmichev.design_patterns.structural

import java.io.{BufferedInputStream, BufferedReader, ByteArrayOutputStream, InputStreamReader}
import java.nio.charset.Charset
import java.util.Base64
import java.util.zip.GZIPOutputStream
import scala.jdk.CollectionConverters.IteratorHasAsScala

// There are cases where we might want to add some extra functionality to a class in an application.
// This could be done via inheritance; however, we might not want to do this or it may affect
// all the other classes in our application. This is where the decorator design pattern is useful.

object Decorator {
  object Basic {
    trait InputReader {
      def readLines(): Stream[String]
    }

    // Basic implementation of InputReader
    class AdvancedInputReader(reader: BufferedReader) extends InputReader {
      override def readLines(): Stream[String] =
        reader.lines().iterator().asScala.toStream
    }

    abstract class InputReaderDecorator(inputReader: InputReader) extends InputReader {
      override def readLines(): Stream[String] =
        inputReader.readLines()
    }

    class CapitalizedInputReader(inputReader: InputReader) extends InputReaderDecorator(inputReader) {
      override def readLines(): Stream[String] =
        super.readLines().map(_.toUpperCase)
    }

    class CompressingInputReader(inputReader: InputReader) extends InputReaderDecorator(inputReader) {
      override def readLines(): Stream[String] = super.readLines().map { case line =>
        val text = line.getBytes(Charset.forName("UTF-8"))

        println("Length before compression: {}", text.length.toString)

        val output     = new ByteArrayOutputStream()
        val compressor = new GZIPOutputStream(output)

        try {
          compressor.write(text, 0, text.length)
          val outputByteArray = output.toByteArray

          println("Length after compression: {}", outputByteArray.length.toString)

          new String(outputByteArray, Charset.forName("UTF-8"))
        } finally {
          compressor.close()
          output.close()
        }
      }
    }

    class Base64EncoderInputReader(inputReader: InputReader) extends InputReaderDecorator(inputReader) {
      override def readLines(): Stream[String] = super.readLines().map { case line =>
        Base64.getEncoder.encodeToString(line.getBytes(Charset.forName("UTF-8")))
      }
    }
  }

  // Abstract override allows us to call super for a method in a trait that is declared abstract.
  // This is permissible for traits as long as the trait is mixed in after another trait
  // or a class that implements the preceding method.
  // The abstract override tells the compiler that we are doing this on purpose
  // and it will not fail our compilation—it will check later, when we use the trait,
  // whether the requirements for using it are satisfied.
  //
  object StackableTrait { // Scala way
    trait InputReader {
      def readLines(): Stream[String]
    }

    // Basic implementation of InputReader
    class AdvancedInputReader(reader: BufferedReader) extends InputReader {
      override def readLines(): Stream[String] =
        reader.lines().iterator().asScala.toStream
    }

    trait CapitalizedInputReaderTrait extends InputReader {
      // Here is the point: abstract override
      abstract override def readLines(): Stream[String] =
        super.readLines().map(_.toUpperCase)
    }

    trait CompressingInputReaderTrait extends InputReader {
      abstract override def readLines(): Stream[String] =
        super.readLines().map { case line =>
          val text = line.getBytes(Charset.forName("UTF-8"))

          println("Length before compression: {}", text.length.toString)

          val output     = new ByteArrayOutputStream()
          val compressor = new GZIPOutputStream(output)

          try {
            compressor.write(text, 0, text.length)
            val outputByteArray = output.toByteArray

            println("Length after compression: {}", outputByteArray.length.toString)

            new String(outputByteArray, Charset.forName("UTF-8"))
          } finally {
            compressor.close()
            output.close()
          }
        }
    }

    trait Base64EncoderInputReaderTrait extends InputReader {
      abstract override def readLines(): Stream[String] =
        super.readLines().map { case line =>
          Base64.getEncoder.encodeToString(line.getBytes(Charset.forName("UTF-8")))
        }
    }

  }
}

object DecoratorExample {
  import Decorator.Basic._

  def main(args: Array[String]): Unit = {
    val stream = new BufferedReader(
      new InputStreamReader(
        new BufferedInputStream(this.getClass.getClassLoader.getResourceAsStream("data.txt"))
      )
    )
    try {
      val reader = new CapitalizedInputReader(new AdvancedInputReader(stream))
      reader.readLines().foreach(println)
    } finally {
      stream.close()
    }
  }
}

object MultiDecoratorExample {
  import Decorator.Basic._

  def main(args: Array[String]): Unit = {
    val stream = new BufferedReader(
      new InputStreamReader(
        new BufferedInputStream(this.getClass.getClassLoader.getResourceAsStream("data.txt"))
      )
    )
    try {
      val reader = new CompressingInputReader(
        new Base64EncoderInputReader(
          new CapitalizedInputReader(
            new AdvancedInputReader(stream)
          )
        )
      )
      reader.readLines().foreach(println)
    } finally {
      stream.close()
    }
  }
}

object StackableTraitsExample {
  import Decorator.StackableTrait._

  def main(args: Array[String]): Unit = {
    val stream = new BufferedReader(
      new InputStreamReader(
        new BufferedInputStream(this.getClass.getClassLoader.getResourceAsStream("data.txt"))
      )
    )
    try {
      val reader = new AdvancedInputReader(stream) with CapitalizedInputReaderTrait
      reader.readLines().foreach(println)
    } finally {
      stream.close()
    }
  }
}

object StackableTraitsMultiExample {
  import Decorator.StackableTrait._

  def main(args: Array[String]): Unit = {
    val stream = new BufferedReader(
      new InputStreamReader(
        new BufferedInputStream(this.getClass.getClassLoader.getResourceAsStream("data.txt"))
      )
    )
    try {
      val reader =
        new AdvancedInputReader(stream)
          with CapitalizedInputReaderTrait
          with Base64EncoderInputReaderTrait
          with CompressingInputReaderTrait

      reader.readLines().foreach(println)
    } finally {
      stream.close()
    }
  }
}
