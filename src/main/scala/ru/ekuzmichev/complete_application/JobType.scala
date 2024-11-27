package ru.ekuzmichev.complete_application

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}

sealed trait JobType
object JobType {
  case object Console extends JobType
  case object Sql     extends JobType
}

object JobTypeSerializer
    extends CustomSerializer[JobType](_ =>
      (
        {
          case JString(jobType) =>
            jobType match {
              case "Console" => JobType.Console
              case "Sql"     => JobType.Sql
            }
          case JNull => null
        },
        { case jobType: JobType =>
          JString(jobType.getClass.getSimpleName.replace("$", ""))
        }
      )
    )
