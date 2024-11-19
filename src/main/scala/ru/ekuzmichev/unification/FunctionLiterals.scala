package ru.ekuzmichev.unification

class FunctionLiterals {
  val sum = (a: Int, b: Int) => a + b

  def runOperation(f: (Int, Int) => Int, a: Int, b: Int): Int = f(a, b)
}

object FunctionLiterals {

  def main(args: Array[String]): Unit = {
    val obj = new FunctionLiterals
    System.out.println(s"3 + 9 = ${obj.sum(3, 9)}") // essentially invoking function's apply method

    println { obj.runOperation(obj.sum, 10, 20) }
    println { obj.runOperation(Math.max, 10, 20) }
  }
}
