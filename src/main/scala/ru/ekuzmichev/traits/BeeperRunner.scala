package ru.ekuzmichev.traits

object BeeperRunner {
  val TIMES = 10

  def main(args: Array[String]): Unit = {
    val beeper = new Beeper {} // Actual instantiation of a class-like trait
    beeper.beep(TIMES)
  }
}
