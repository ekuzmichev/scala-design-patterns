package ru.ekuzmichev.traits

object ReallyExpensiveWatchUser {
  def main(args: Array[String]): Unit = {

    // We use the ConnectorWithHelper trait defined previously. This trait extends the abstract Connector class
    // ❌ We got compilation error:
    //  illegal inheritance
    //    superclass Watch is not a subclass of
    //    the superclass Connector of the mixin trait ConnectorWithHelper
    //
    // All the classes that use ConnectorWithHelper trait for composition must be subclasses of Connector (it is abstract class)
    //
    // If we want to fix the compilation issue in the example, we will have to modify the original Watch class
    // and make sure it is a subclass of Connector.
    // This, however, might not be desired and some refactoring might be needed in such cases.
    val reallyExpensiveWatch = new Watch("really expensive brand", 1000L) with ConnectorWithHelper {
      override def connect(): Unit = {
        System.out.println("Connected with another connector.")
      }
      override def close(): Unit = {
        System.out.println("Closed with another connector.")
      }
    }

    System.out.println("Using the really expensive watch.")
    reallyExpensiveWatch.findDriver()
    reallyExpensiveWatch.connect()
    reallyExpensiveWatch.close()
  }
}
