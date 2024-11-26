package ru.ekuzmichev.design_patterns.behavioral

import scala.collection.mutable

// The purpose of the observer design pattern is to have an object (called subject)
// that automatically notifies all of its observers of any state change by calling one of their methods.
//
object Observer {
  trait Observer[T] {
    def handleUpdate(subject: T): Unit
  }

  trait Observable[T] { this: T =>

    private val observers = mutable.ListBuffer[Observer[T]]()

    def addObserver(observer: Observer[T]): Unit =
      observers.+=:(observer)

    def notifyObservers(): Unit =
      observers.foreach(_.handleUpdate(this))
  }

  case class User(name: String) extends Observer[Post] {
    override def handleUpdate(subject: Post): Unit =
      println(s"Hey, I'm $name. The post got some new comments: ${subject.comments.mkString("[", ", ", "]")}")
  }

  case class Comment(user: User, text: String)

  case class Post(user: User, text: String) extends Observable[Post] {
    val comments = mutable.ListBuffer[Comment]()

    def addComment(comment: Comment): Unit = {
      comments.+=:(comment)
      notifyObservers()
    }
  }
}

object ObserverExample {
  import Observer._

  def main(args: Array[String]): Unit = {
    val userIvan  = User("Ivan")
    val userMaria = User("Maria")
    val userJohn  = User("John")

    println("Create a post")
    val post = Post(userIvan, "This is a post about the observer design pattern")

    println("Add a comment")
    post.addComment(Comment(userIvan, "I hope you like the post!"))

    println("John and Maria subscribe to the comments.")
    post.addObserver(userJohn)
    post.addObserver(userMaria)

    println("Add a comment")
    post.addComment(Comment(userIvan, "Why are you so quiet? Do you like it?"))

    println("Add a comment")
    post.addComment(Comment(userMaria, "It is amazing! Thanks!"))
  }
}
