package ru.ekuzmichev.design_patterns.behavioral

import java.util.Random
import java.util.concurrent.ConcurrentLinkedQueue

// The purpose of the null object design pattern is to define an actual object
// that represents the null value and has neutral behavior.
//
object NullObject {
  case class Message(number: Int) {
    def print(): String = s"This is a message with number: $number."
  }

  class DataGenerator extends Runnable {
    val MAX_VAL  = 10
    val MAX_TIME = 10000

    private var isStop                            = false
    private val queue: ConcurrentLinkedQueue[Int] = new ConcurrentLinkedQueue[Int]()

    override def run(): Unit = {
      val random = new Random()
      while (!isStop) {
        Thread.sleep(random.nextInt(MAX_TIME))
        queue.add(random.nextInt(MAX_VAL))
      }
    }

    def getMessage(): Option[Message] =
      Option(queue.poll()).map { case number =>
        Message(number)
      }

    def requestStop(): Unit = this.synchronized {
      isStop = true
    }
  }
}

object NullObjectExample {
  import NullObject._

  val TIMES_TO_TRY = 10
  val MAX_TIME     = 5000

  def main(args: Array[String]): Unit = {
    val generator = new DataGenerator
    // start the generator in another thread
    new Thread(generator).start()
    val random = new Random()
    (0 to TIMES_TO_TRY).foreach { case time =>
      Thread.sleep(random.nextInt(MAX_TIME))
      System.out.println("Getting next message...")
      generator.getMessage().foreach(m => System.out.println(m.print()))
    }
    generator.requestStop()
  }
}
