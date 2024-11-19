package ru.ekuzmichev.traits

// Self-type annotation is for the case when we might actually want
// to enforce a trait to be mixed into a class that also has
// another trait or multiple traits mixed into it
//
// Self-type annotation brings all the methods of Notifier to the scope
// of our new trait, and it also requires that any class that mixes in AlarmNotifier
// should also mix in Notifier. Otherwise, a compilation error will occur
trait AlarmNotifier {
  this: Notifier =>

  def trigger(): String
}
