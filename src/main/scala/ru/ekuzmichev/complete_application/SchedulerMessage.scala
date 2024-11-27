package ru.ekuzmichev.complete_application

sealed trait SchedulerMessage

object SchedulerMessage {
  case class Work(name: String, command: String, jobType: JobType)                   extends SchedulerMessage
  case class Done(name: String, command: String, jobType: JobType, success: Boolean) extends SchedulerMessage
  case class Schedule(configs: List[JobConfig])                                      extends SchedulerMessage
}
