package ru.ekuzmichev.design_patterns.structural

import java.io.{BufferedReader, InputStreamReader}
import scala.jdk.CollectionConverters.IteratorHasAsScala

// The purpose of the proxy design pattern is to provide an interface
// to something else that then gets served behind the scenes to the user.
//
// The proxy design pattern is another example of a wrapper.
// It is pretty similar to the decorator design pattern, but feels more basic and limited.
// The reason for this is that the relationship between the proxy and the wrapped object
// is established during compile time and decorators could be applied at runtime.
// In the end, its purpose is different.
//
object Proxy {
  trait FileReader {
    def readFileContents(): String
  }

  class FileReaderReal(filename: String) extends FileReader {
    private val contents: String = {
      val stream = this.getClass.getClassLoader.getResourceAsStream(filename)
      val reader = new BufferedReader(new InputStreamReader(stream))
      try {
        reader.lines().iterator().asScala.mkString(java.lang.System.lineSeparator())
      } finally {
        reader.close()
        stream.close()
      }
    }

    println(s"Finished reading the actual file: $filename")

    override def readFileContents(): String = contents
  }

  class FileReaderProxy(filename: String) extends FileReader {
    private lazy val fileReader: FileReaderReal = new FileReaderReal(filename)

    override def readFileContents(): String =
      fileReader.readFileContents()
  }
}

object ProxyExample {
  import Proxy._

  def main(args: Array[String]): Unit = {
    val fileMap = Map(
      "file1.txt" -> new FileReaderProxy("file1.txt"),
      "file2.txt" -> new FileReaderProxy("file2.txt"),
      "file3.txt" -> new FileReaderProxy("file3.txt"),
      "file4.txt" -> new FileReaderReal("file1.txt")
    )
    System.out.println("Created the map. You should have seen file1.txt read because it wasn't used in a proxy.")
    System.out.println(s"Reading file1.txt from the proxy: ${fileMap("file1.txt").readFileContents()}")
    System.out.println(s"Reading file3.txt from the proxy: ${fileMap("file3.txt").readFileContents()}")
  }
}
