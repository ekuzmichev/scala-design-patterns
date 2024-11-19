package ru.ekuzmichev.traits

// We can add multiple traits to a class | Here object AKA 'singleton class' is used
//
// Mixing traits into a class is done with the following syntax:
// extends T1 with T2 with ... with Tn
//
// If a trait method is not implemented inside the trait body and the class
// we are mixing it into is not abstract, the class will have to implement the trait
object MixinRunner extends Ping with Pong {
  def main(args: Array[String]): Unit = {
    ping()
    pong()
  }
}
