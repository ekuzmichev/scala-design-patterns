package ru.ekuzmichev.complete_application

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, FiniteDuration}

case class TimeOptions(hours: Int, minutes: Int) {
  require(hours >= 0 && hours <= 23, "Hours must be between 0 and 23: " + hours)
  require(minutes >= 0 && minutes <= 59, "Minutes must be between 0 and 59: " + minutes)

  def getInitialDelay(now: LocalDateTime, frequency: JobFrequency): FiniteDuration = {
    val firstRun = now.withHour(hours).withMinute(minutes)
    val isBefore = firstRun.isBefore(now)
    val actualFirstRun = frequency match {
      case JobFrequency.Hourly =>
        var tmp = firstRun
        Iterator
          .continually({ tmp = tmp.plusHours(1); tmp })
          .takeWhile(d => d.isBefore(now))
          .toList
          .lastOption
          .getOrElse(if (isBefore) firstRun else firstRun.minusHours(1))
          .plusHours(1)
      case JobFrequency.Daily =>
        var tmp = firstRun
        Iterator
          .continually({ tmp = tmp.plusDays(1); tmp })
          .takeWhile(d => d.isBefore(now))
          .toList
          .lastOption
          .getOrElse(if (isBefore) firstRun else firstRun.minusDays(1))
          .plusDays(1)
    }
    val secondsUntilRun = now.until(actualFirstRun, ChronoUnit.SECONDS)
    Duration.create(secondsUntilRun, TimeUnit.SECONDS)
  }

}
