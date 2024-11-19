package ru.ekuzmichev.unification

/*

Modules are a way to organize programs. They are interchangeable and pluggable pieces
of code that have well-defined interfaces and hidden implementations.

In Java, modules are organized in packages.
In Scala, modules are objects, just like everything else.
This means that they can be parameterized, extended, and passed as parameters, and so on.

Scala modules can provide requirements in order to be used.

Modules in Scala can be passed as any other object.
They are extendable, interchangeable, and their implementation is hidden.

 */

// Tick is just an interface to one of our modules
trait Tick {
  trait Ticker {
    def count(): Int
    def tick(): Unit
  }
  def ticker: Ticker
}

// TickUser is a module implementation (an actual module)
trait TickUser extends Tick {
  class TickUserImpl extends Ticker {
    var curr = 0

    override def count(): Int = curr

    override def tick(): Unit = {
      curr = curr + 1
    }
  }
  // We create a singleton object that will carry the implementation
  // The name of the object is the same as the method in Tick.
  //  This would cover the need to implement it when mixing in the trait.
  object ticker extends TickUserImpl
}

trait Alarm {
  trait Alarmer {
    def trigger(): Unit
  }
  def alarm: Alarmer
}

// We extended both modules in the AlarmUser one.
// This shows how modules could be made to be dependent on each other.
trait AlarmUser extends Alarm with Tick {
  class AlarmUserImpl extends Alarmer {
    override def trigger(): Unit = {
      if (ticker.count() % 10 == 0) {
        System.out.println(s"Alarm triggered at ${ticker.count()}!")
      }
    }
  }
  object alarm extends AlarmUserImpl
}

object ModuleDemo extends AlarmUser with TickUser {
  def main(args: Array[String]): Unit = {
    System.out.println("Running the ticker. Should trigger the alarm every 10 times.")
    (1 to 100).foreach { case i =>
      ticker.tick()
      alarm.trigger()
    }
  }
}
