package ru.ekuzmichev.design_patterns.behavioral

import com.github.tototoshi.csv.CSVReader
import org.json4s._
import org.json4s.jackson.JsonMethods

import java.io.InputStreamReader

// The strategy design pattern enables us to define a family of algorithms and select a specific one at runtime.
//
// The strategy design pattern helps with encapsulation as each algorithm can be separately defined
// and then injected into the classes that use it. The different implementations are also interchangeable.
//
object Strategy {
  object ClassicOop {
    case class Person(name: String, age: Int, address: String)

    trait Parser[T] {
      def parse(file: String): List[T]
    }

    class CSVParser extends Parser[Person] {
      override def parse(file: String): List[Person] = {
        val inputStream       = getClass.getClassLoader.getResourceAsStream(file)
        val inputStreamReader = new InputStreamReader(inputStream)
        CSVReader.open(inputStreamReader).all().map { case List(name, age, address) =>
          Person(name, age.toInt, address)
        }
      }
    }

    class JsonParser extends Parser[Person] {
      private implicit val formats: Formats = DefaultFormats

      override def parse(file: String): List[Person] = {
        val inputStream = getClass.getClassLoader.getResourceAsStream(file)
        JsonMethods.parse(inputStream).extract[List[Person]]
      }
    }

    object Parser {
      def apply(filename: String): Parser[Person] =
        if (filename.endsWith(".json")) new JsonParser
        else if (filename.endsWith(".csv")) new CSVParser
        else throw new RuntimeException(s"Unknown format: $filename")
    }

    class PersonApplication[T](parser: Parser[T]) {
      def write(file: String): Unit = {
        println(s"Got the following data ${parser.parse(file)}")
      }
    }
  }

  object ScalaWay {
    case class Person(name: String, age: Int, address: String)

    class Application[T](strategy: String => List[T]) {
      def write(file: String): Unit = {
        println(s"Got the following data ${strategy(file)}")
      }
    }

    object StrategyFactory {
      implicit val formats = DefaultFormats

      def apply(filename: String): String => List[Person] =
        filename match {
          case f if f.endsWith(".json") => parseJson
          case f if f.endsWith(".csv")  => parseCsv
          case f                        => throw new RuntimeException(s"Unknown format: $f")
        }

      def parseJson(file: String): List[Person] =
        JsonMethods.parse(getClass.getClassLoader.getResourceAsStream(file)).extract[List[Person]]

      def parseCsv(file: String): List[Person] =
        CSVReader.open(new InputStreamReader(getClass.getClassLoader.getResourceAsStream(file))).all().map {
          case List(name, age, address) => Person(name, age.toInt, address)
        }
    }
  }
}

object StrategyClassicOopExample {
  import Strategy.ClassicOop._

  def main(args: Array[String]): Unit = {
    val csvPeople  = Parser("persons.csv")
    val jsonPeople = Parser("persons.json")

    val applicationCsv  = new PersonApplication(csvPeople)
    val applicationJson = new PersonApplication(jsonPeople)

    println("Using the csv: ")
    applicationCsv.write("persons.csv")

    println("Using the json: ")
    applicationJson.write("persons.json")
  }
}

object StrategyScalaWayExample {
  import Strategy.ScalaWay._

  def main(args: Array[String]): Unit = {
    val applicationCsv  = new Application[Person](StrategyFactory("persons.csv"))
    val applicationJson = new Application[Person](StrategyFactory("persons.json"))

    println("Using the csv: ")
    applicationCsv.write("persons.csv")

    println("Using the json: ")
    applicationJson.write("persons.json")
  }
}
