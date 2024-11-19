package ru.ekuzmichev.traits

// This is like a class with only one constructor with no parameters
trait Beeper {
  def beep(times: Int): Unit = {
    1 to times foreach (i => System.out.println(s"Beep number: $i"))
  }
}
