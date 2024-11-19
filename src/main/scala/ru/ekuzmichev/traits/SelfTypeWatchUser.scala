package ru.ekuzmichev.traits

object SelfTypeWatchUser {
  def main(args: Array[String]): Unit = {
    // ❌Uncomment to see the self-type error.
    // val watch = new Watch("alarm with notification", 1000L) with AlarmNotifier {}

    val watch = new Watch("alarm with notification", 1000L) with AlarmNotifier with Notifier {
      override def trigger(): String = "Alarm triggered."

      override def clear(): Unit = {
        System.out.println("Alarm cleared.")
      }

      override val notificationMessage: String = "The notification."
    }

    System.out.println(watch.trigger())
    watch.printNotification()
    System.out.println(s"The time is ${watch.getTime()}.")
    watch.clear()
  }
}
