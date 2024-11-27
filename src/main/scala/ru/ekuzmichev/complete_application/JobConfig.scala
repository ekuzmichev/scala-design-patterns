package ru.ekuzmichev.complete_application

import org.json4s.{FieldSerializer, JField}

case class JobConfig(
    name: String,
    command: String,
    jobType: JobType,
    frequency: JobFrequency,
    timeOptions: TimeOptions
)

object JobConfig {
  val jobConfigFieldSerializer: FieldSerializer[JobConfig] = FieldSerializer[JobConfig](
    {
      case (CaseClassKeys.TypeOptions, x) => Some(JsonKeys.TimeOptions, x)
      case (CaseClassKeys.JobType, x)     => Some(JsonKeys.Type, x)
    },
    {
      case JField(JsonKeys.TimeOptions, x) => JField(CaseClassKeys.TypeOptions, x)
      case JField(JsonKeys.Type, x)        => JField(CaseClassKeys.JobType, x)
    }
  )

  private object CaseClassKeys {
    val TypeOptions = "timeOptions"
    val JobType     = "jobType"
  }

  private object JsonKeys {
    val TimeOptions = "time_options"
    val Type        = "type"
  }
}
