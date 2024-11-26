package ru.ekuzmichev.design_patterns.behavioral

import com.github.tototoshi.csv.CSVReader
import org.json4s.jackson.JsonMethods
import org.json4s.{DefaultFormats, Formats}

import java.io.{ByteArrayInputStream, InputStreamReader}

// The purpose of the template method design pattern is to defer
// algorithm steps to subclasses using template methods.
//
// The template method design pattern seems really natural to object-oriented programming.
// Whenever polymorphism is used, this actually represents the design pattern itself.
// Usually, the template method is implemented using abstract methods.
//
object TemplateMethod {
  // Template steps:
  //
  // - Read the data
  // - Parse the data
  // - Search for items satisfying the condition
  // - Clean up any resources if needed

  case class Person(name: String, age: Int, address: String)

  trait DataFinder[T, Y] {
    def find(f: T => Option[Y]): Option[Y] =
      try {
        val data   = readData()
        val parsed = parse(data)
        f(parsed)
      } finally {
        cleanup()
      }

    def readData(): Array[Byte]

    def parse(data: Array[Byte]): T

    def cleanup(): Unit
  }

  class JsonDataFinder extends DataFinder[List[Person], Person] {
    implicit val formats: Formats = DefaultFormats

    override def readData(): Array[Byte] = {
      val stream = this.getClass.getClassLoader.getResourceAsStream("persons.json")
      Stream.continually(stream.read).takeWhile(_ != -1).map(_.toByte).toArray
    }

    override def cleanup(): Unit =
      println("Reading json: nothing to do.")

    override def parse(data: Array[Byte]): List[Person] =
      JsonMethods.parse(new String(data, "UTF-8")).extract[List[Person]]
  }

  class CSVDataFinder extends DataFinder[List[Person], Person] {
    override def readData(): Array[Byte] = {
      val stream = this.getClass.getClassLoader.getResourceAsStream("persons.csv")
      Stream.continually(stream.read).takeWhile(_ != -1).map(_.toByte).toArray
    }

    override def cleanup(): Unit =
      System.out.println("Reading csv: nothing to do.")

    override def parse(data: Array[Byte]): List[Person] =
      CSVReader.open(new InputStreamReader(new ByteArrayInputStream(data))).all().map { case List(name, age, address) =>
        Person(name, age.toInt, address)
      }
  }
}

object TemplateMethodExample {
  import TemplateMethod._

  def main(args: Array[String]): Unit = {
    val jsonDataFinder: DataFinder[List[Person], Person] = new JsonDataFinder
    val csvDataFinder: DataFinder[List[Person], Person]  = new CSVDataFinder

    System.out.println(s"Find a person with name Ivan in the json: ${jsonDataFinder.find(_.find(_.name == "Ivan"))}")
    System.out.println(s"Find a person with name James in the json: ${jsonDataFinder.find(_.find(_.name == "James"))}")
    System.out.println(s"Find a person with name Maria in the csv: ${csvDataFinder.find(_.find(_.name == "Maria"))}")
    System.out.println(s"Find a person with name Alice in the csv: ${csvDataFinder.find(_.find(_.name == "Alice"))}")
  }
}
