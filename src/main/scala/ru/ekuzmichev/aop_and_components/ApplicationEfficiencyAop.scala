package ru.ekuzmichev.aop_and_components

case class Person(firstName: String, lastName: String, age: Int)

trait DataReader {
  def readData(): List[Person]
  def readDataInefficiently(): List[Person]
}

class DataReaderImpl extends DataReader {
  private def readUntimed(): List[Person] =
    List(Person("Bob", "Marley", 55))

  override def readData(): List[Person] = readUntimed()

  override def readDataInefficiently(): List[Person] = {
    (1 to 100000000).foreach { case num =>
      readUntimed()
    }
    readUntimed()
  }
}

trait LoggingDataReader extends DataReader {

  // Here is abstract override modifier.
  // It notifies the compiler that we will be doing stackable modifications
  abstract override def readData(): List[Person] = {
    val startMillis = System.currentTimeMillis()
    val result      = super.readData()
    val time        = System.currentTimeMillis() - startMillis
    System.err.println(s"readData took ${time} milliseconds.")
    result
  }

  abstract override def readDataInefficiently(): List[Person] = {
    val startMillis = System.currentTimeMillis()
    val result      = super.readDataInefficiently()
    val time        = System.currentTimeMillis() - startMillis
    System.err.println(s"readDataInefficiently took ${time} milliseconds.")
    result
  }
}

object DataReaderExample {
  def main(args: Array[String]): Unit = {
    val dataReader = new DataReaderImpl with LoggingDataReader
    System.out.println(s"I just read the following data efficiently: ${dataReader.readData()}")
    System.out.println(s"I just read the following data inefficiently: ${dataReader.readDataInefficiently()}")
  }
}
