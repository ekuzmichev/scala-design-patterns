package ru.ekuzmichev.traits

trait FormalGreeting {
  def hello(): String
}

trait InformalGreeting {
  def hello(): String
}

// While implementing, it just has to implement the method once.
class Greeter extends FormalGreeting with InformalGreeting {
  override def hello(): String = "Good morning, sir/madam!"
}

object GreeterUser {
  def main(args: Array[String]): Unit = {
    val greeter = new Greeter()
    System.out.println(greeter.hello())
  }
}
