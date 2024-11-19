package ru.ekuzmichev.traits

// This is a mixin
trait Notifier {
  val notificationMessage: String

  def printNotification(): Unit = {
    System.out.println(notificationMessage)
  }

  def clear()
}
