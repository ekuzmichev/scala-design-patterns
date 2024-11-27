package ru.ekuzmichev.complete_application

import akka.actor.{ActorSystem, Props}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object SchedulerApplication extends LazyLogging {
  import ComponentRegistry._

  def main(args: Array[String]): Unit = {
    logger.info("Running migrations before doing anything else.")

    migrationService.runMigrations()

    logger.info("Migrations done!")

    val system = ActorSystem("scheduler")

    val master = system.actorOf(
      Props(actorFactory.createMasterActor()),
      "scheduler-master"
    )

    sys.addShutdownHook({
      logger.info("Awaiting actor system termination.")
      // not great...
      Await.result(system.terminate(), Duration.Inf)
      logger.info("Actor system terminated. Bye!")
    })

    master ! SchedulerMessage.Schedule(jobConfigReaderService.readJobConfigs())
    logger.info("Started! Use CTRL+C to exit.")
  }
}
