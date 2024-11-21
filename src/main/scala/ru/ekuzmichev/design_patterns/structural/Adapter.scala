package ru.ekuzmichev.design_patterns.structural

// In many cases, we have to make applications work by combining different components together.
// However, quite often, we have a problem where the component interfaces are incompatible with each other.
// Similarly with using public or any libraries, which we cannot modify ourselves, it is quite rare
// that someone else's views will be exactly the same as ours in our current settings.
// This is where adapters help. Their purpose is to help incompatible interfaces work together
// without modifying their source code.
//
object Adapter {
  object ExtendableClass {
    // This is assumed non-changeable (e.g. external library)
    class Logger {
      def log(message: String, severity: String): Unit = {
        System.out.println(s"${severity.toUpperCase}: $message")
      }
    }

    // Interface for abstraction
    trait Log {
      def info(message: String)
      def debug(message: String)
      def warning(message: String)
      def error(message: String)
    }

    // Adapter
    class AppLogger extends Logger with Log {
      override def info(message: String): Unit    = log(message, "info")
      override def warning(message: String): Unit = log(message, "warning")
      override def error(message: String): Unit   = log(message, "error")
      override def debug(message: String): Unit   = log(message, "debug")
    }
  }

  object FinalClass {
    // This is assumed non-changeable (e.g. external library)
    final class FinalLogger {
      def log(message: String, severity: String): Unit = {
        System.out.println(s"${severity.toUpperCase}: $message")
      }
    }

    // Interface for abstraction
    trait Log {
      def info(message: String)
      def debug(message: String)
      def warning(message: String)
      def error(message: String)
    }

    class FinalAppLogger extends Log {
      // Could be also a constructor parameter
      private val logger = new FinalLogger

      override def info(message: String): Unit    = logger.log(message, "info")
      override def warning(message: String): Unit = logger.log(message, "warning")
      override def error(message: String): Unit   = logger.log(message, "error")
      override def debug(message: String): Unit   = logger.log(message, "debug")
    }
  }

  object ScalaWay {
    // This is assumed non-changeable (e.g. external library)
    final class FinalLogger {
      def log(message: String, severity: String): Unit = {
        System.out.println(s"${severity.toUpperCase}: $message")
      }
    }

    // Interface for abstraction
    trait Log {
      def info(message: String)
      def debug(message: String)
      def warning(message: String)
      def error(message: String)
    }

    implicit class FinalAppLoggerImplicit(logger: FinalLogger) extends Log {
      override def info(message: String): Unit    = logger.log(message, "info")
      override def warning(message: String): Unit = logger.log(message, "warning")
      override def error(message: String): Unit   = logger.log(message, "error")
      override def debug(message: String): Unit   = logger.log(message, "debug")
    }
  }
}

object ExtendableClassExample {
  import Adapter.ExtendableClass._

  def main(args: Array[String]): Unit = {
    val logger = new AppLogger
    logger.info("This is an info message.")
    logger.debug("Debug something here.")
    logger.error("Show an error message.")
    logger.warning("About to finish.")
    logger.info("Bye!")
  }
}

object FinalClassExample {
  import Adapter.FinalClass._

  def main(args: Array[String]): Unit = {
    val logger = new FinalAppLogger
    logger.info("This is an info message.")
    logger.debug("Debug something here.")
    logger.error("Show an error message.")
    logger.warning("About to finish.")
    logger.info("Bye!")
  }
}

object ScalaWayExample {
  import Adapter.ScalaWay._

  def main(args: Array[String]): Unit = {
    val logger = new FinalLogger

    logger.info("This is an info message.")
    logger.debug("Debug something here.")
    logger.error("Show an error message.")
    logger.warning("About to finish.")
    logger.info("Bye!")
  }
}
