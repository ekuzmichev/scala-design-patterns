package ru.ekuzmichev.traits

trait AA {
  def hello(): String = "Hello from AA"
}

trait BB extends AA {
  override def hello(): String = "Hello from BB"
}

trait CC extends AA {
  override def hello(): String = "Hello from CC"
}

trait DD extends BB with CC {}

object Diamond extends DD {
  def main(args: Array[String]): Unit = {
    // The output is 'Hello from CC' due to linearization
    System.out.println(hello())
  }
}
