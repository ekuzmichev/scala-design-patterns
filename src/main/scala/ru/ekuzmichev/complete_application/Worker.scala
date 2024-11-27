package ru.ekuzmichev.complete_application

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging

import scala.sys.process._

class Worker(daoService: DaoService) extends Actor with LazyLogging {

  override def receive: Receive = { case work: SchedulerMessage.Work => doWork(work) }

  private def doWork(work: SchedulerMessage.Work): Unit = {
    work.jobType match {
      case JobType.Console =>
        val result = work.command.! // note - the ! are different methods
        sender ! SchedulerMessage.Done(work.name, work.command, work.jobType, result == 0)
      case JobType.Sql =>
        val connection = daoService.getConnection()
        try {
          val statement = connection.prepareStatement(work.command)
          val result: List[String] = daoService.executeSelect(statement) { rs =>
            val metadata   = rs.getMetaData
            val numColumns = metadata.getColumnCount
            daoService.readResultSet(rs) { row =>
              (1 to numColumns)
                .map(row.getObject)
                .mkString("\t")
            }
          }
          logger.info("Sql query results: ")
          result.foreach(r => logger.info(r))
          sender ! SchedulerMessage.Done(work.name, work.command, work.jobType, success = true)
        } finally {
          connection.close()
        }
    }
  }
}
