package ru.ekuzmichev.design_patterns.structural

import org.json4s._
import org.json4s.jackson.JsonMethods

import java.util.Base64

// The purpose of the facade design pattern is to wrap a complex system
// with a simpler interface in order to hide the usage complexities
// and ease the client interaction.
//
// The facade design pattern is useful when we want to hide the
// implementation details of many libraries, make an interface
// much easier to use, and interact with complex systems.
//
object Facade {
  case class Person(name: String, age: Int)

  trait DataDecoder {
    def decode(data: Array[Byte]): String =
      new String(decodeBase64(data), "UTF-8")

    private def decodeBase64(data: Array[Byte]): Array[Byte] =
      Base64.getDecoder.decode(data)
  }

  trait DataDeserializer {
    private implicit val formats: Formats = DefaultFormats

    def parse[T: Manifest](data: String): T = JsonMethods.parse(data).extract[T]
  }

  trait DataDownloader {
    def download(url: String): Array[Byte] = {
      println(s"Downloading from: $url")

      Thread.sleep(5000)

      //    {
      //      "name": "Ivan",
      //      "age": 26
      //    }
      //
      //    The string below is the Base64 encoded Json above.

      "ew0KICAgICJuYW1lIjogIkl2YW4iLA0KICAgICJhZ2UiOiAyNg0KfQ==".getBytes
    }
  }

  class DataReader extends DataDownloader with DataDecoder with DataDeserializer {
    def readPerson(url: String): Person = {
      val data: Array[Byte] = download(url)
      val json: String      = decode(data)
      parse[Person](json)
    }
  }
}

object FacadeExample {
  import Facade._

  def main(args: Array[String]): Unit = {
    val reader = new DataReader
    println(s"""We just read the following person: ${reader.readPerson("http://example.com")}""")
  }
}
