package ru.ekuzmichev.design_patterns.creational

import scala.collection.concurrent.TrieMap
import scala.collection.mutable

object StatefulSingleton {
  object AppRegistry {
    // Singletons in Scala are lazily initialized
    // While creating a singleton instance, we cannot provide dynamic parameters to the singleton class instance
    System.out.println("Registry initialization block called.")
    private val users: mutable.Map[String, String] = TrieMap.empty

    def addUser(id: String, name: String): Unit = {
      users.put(id, name)
    }

    def removeUser(id: String): Unit = {
      users.remove(id)
    }

    def isUserRegistered(id: String): Boolean = users.contains(id)

    def getAllUserNames(): List[String] = users.map(_._2).toList
  }
}

object StatefulSingletonExample {
  import StatefulSingleton._

  def main(args: Array[String]): Unit = {
    System.out.println("Sleeping for 5 seconds.")
    Thread.sleep(5000)
    System.out.println("I woke up.")
    AppRegistry.addUser("1", "Ivan")
    AppRegistry.addUser("2", "John")
    AppRegistry.addUser("3", "Martin")
    System.out.println(s"Is user with ID=1 registered? ${AppRegistry.isUserRegistered("1")}")
    System.out.println("Removing ID=2")
    AppRegistry.removeUser("2")
    System.out.println(s"Is user with ID=2 registered? ${AppRegistry.isUserRegistered("2")}")
    System.out.println(s"All users registered are: ${AppRegistry.getAllUserNames().mkString(",")}")
  }
}
