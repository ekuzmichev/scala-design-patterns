package ru.ekuzmichev.traits

class Watch(brand: String, initialTime: Long) {
  def getTime(): Long = System.currentTimeMillis() - initialTime
}

object WatchUser {
  def main(args: Array[String]): Unit = {

    // Here we compose simple traits, which do not extend other traits or classes
    // They are anonymous classes, which are defined during instantiation.

    val expensiveWatch = new Watch("expensive brand", 1000L) with Alarm with Notifier {
      override def trigger(): String           = "The alarm was triggered."
      override def clear(): Unit               = System.out.println("Alarm cleared.")
      override val notificationMessage: String = "Alarm is running!"
    }

    val cheapWatch = new Watch("cheap brand", 1000L) with Alarm {
      override def trigger(): String = "The alarm was triggered."
    }

    // show some watch usage.
    System.out.println(expensiveWatch.trigger())
    expensiveWatch.printNotification()
    System.out.println(s"The time is ${expensiveWatch.getTime()}.")
    expensiveWatch.clear()

    System.out.println(cheapWatch.trigger())
    System.out.println("Cheap watches cannot manually stop the alarm...")
  }
}
