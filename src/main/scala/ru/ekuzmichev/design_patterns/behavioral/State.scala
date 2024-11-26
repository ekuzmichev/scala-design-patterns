package ru.ekuzmichev.design_patterns.behavioral

// The purpose of the state design pattern is to allow us to choose
// a different behavior of an object based on the object's internal state.
//
// Basically, the difference between the state design pattern and the strategy
// design pattern comes from the following two points:
//
//- The strategy design pattern is about how an action is performed.
//    It is usually an algorithm that produces the same results as other algorithms.
//- The state design pattern is about what action is performed.
//    Depending on the state, an object could be doing different things.
//
object State {
  trait State[T] {
    def press(context: T): Unit
  }

  object Playing extends State[MediaPlayer] {
    override def press(context: MediaPlayer): Unit = {
      println("Pressing pause.")
      context.setState(Paused)
    }
  }

  object Paused extends State[MediaPlayer] {
    override def press(context: MediaPlayer): Unit = {
      println("Pressing play.")
      context.setState(Playing)
    }
  }

  case class MediaPlayer() {
    private var state: State[MediaPlayer] = Paused

    def pressPlayOrPauseButton(): Unit =
      state.press(this)

    def setState(state: State[MediaPlayer]): Unit =
      this.state = state
  }
}

object StateExample {
  import State._

  def main(args: Array[String]): Unit = {
    val player = MediaPlayer()
    player.pressPlayOrPauseButton()
    player.pressPlayOrPauseButton()
    player.pressPlayOrPauseButton()
    player.pressPlayOrPauseButton()
  }
}
