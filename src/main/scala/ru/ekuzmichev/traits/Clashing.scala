package ru.ekuzmichev.traits

trait A {
  def hello(): String = "Hello, I am trait A!"
}

trait B {
  def hello(): String = "Hello, I am trait B!"
}

/*
// object Clashing inherits conflicting members:
//  def hello(): String (defined in trait A) and
//  def hello(): String (defined in trait B)
//  (note: this can be resolved by declaring an `override` in object Clashing.)
object Clashing extends A with B {
  def main(args: Array[String]): Unit = {
    System.out.println(hello())
  }
}
 */

object Clashing extends AA with BB {
  def main(args: Array[String]): Unit = {
    System.out.println(hello())
  }

  override def hello(): String =
    super.hello() // Hello, I am trait B!
  // super[A].hello() // Hello, I am trait A!
}
