package ru.ekuzmichev.complete_application

import akka.actor.{Actor, ActorRef, Cancellable, Props}
import akka.routing.RoundRobinPool
import com.typesafe.scalalogging.LazyLogging

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration

class Master(numWorkers: Int, actorFactory: ActorFactory) extends Actor with LazyLogging {
  private val cancellables: ListBuffer[Cancellable] = ListBuffer[Cancellable]()

  private val router: ActorRef = context.actorOf(
    Props(actorFactory.createWorkerActor()).withRouter(RoundRobinPool(numWorkers)),
    "scheduler-master-worker-router"
  )
  override def receive: Receive = {
    case SchedulerMessage.Done(name, command, jobType, success) =>
      if (success)
        logger.info(s"Successfully completed $name ($command)")
      else
        logger.error(s"Failure! Command $name ($command) returned a non-zero result code")
    case SchedulerMessage.Schedule(configs) =>
      configs.foreach { config =>
        val cancellable = this.context.system.scheduler.scheduleWithFixedDelay(
          config.timeOptions.getInitialDelay(LocalDateTime.now(), config.frequency),
          Duration.create(
            1,
            config.frequency match {
              case JobFrequency.Hourly => TimeUnit.HOURS
              case JobFrequency.Daily  => TimeUnit.DAYS
            }
          ),
          router,
          SchedulerMessage.Work(config.name, config.command, config.jobType)
        )(context.dispatcher, self)

        cancellable +: cancellables

        logger.info(s"Scheduled: $config")
      }
  }

  override def postStop(): Unit =
    cancellables.foreach(_.cancel())
}
