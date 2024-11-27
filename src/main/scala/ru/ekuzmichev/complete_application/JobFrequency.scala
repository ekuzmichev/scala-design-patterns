package ru.ekuzmichev.complete_application

import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}

sealed trait JobFrequency

object JobFrequency {
  case object Daily  extends JobFrequency
  case object Hourly extends JobFrequency
}

case object JobFrequencySerializer
    extends CustomSerializer[JobFrequency](_ =>
      (
        {
          case JString(frequency) =>
            frequency match {
              case "Daily"  => JobFrequency.Daily
              case "Hourly" => JobFrequency.Hourly
            }
          case JNull => null
        },
        { case frequency: JobFrequency =>
          JString(frequency.getClass.getSimpleName.replace("$", ""))
        }
      )
    )
