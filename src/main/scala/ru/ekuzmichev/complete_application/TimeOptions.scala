package ru.ekuzmichev.complete_application

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, FiniteDuration}

case class TimeOptions(hours: Int, minutes: Int) {
  require(hours >= 0 && hours <= 23, "Hours must be between 0 and 23: " + hours)
  require(minutes >= 0 && minutes <= 59, "Minutes must be between 0 and 59: " + minutes)

  def getInitialDelay(now: LocalDateTime, jobFrequency: JobFrequency): FiniteDuration = {
    val actualFirstRun: LocalDateTime = calculateActualFirstRun(now, jobFrequency)
    val numOfSecondsUntilRun          = now.until(actualFirstRun, ChronoUnit.SECONDS)
    Duration.create(numOfSecondsUntilRun, TimeUnit.SECONDS)
  }

  private def calculateActualFirstRun(now: LocalDateTime, jobFrequency: JobFrequency): LocalDateTime = {
    val firstRun: LocalDateTime = now.withHour(hours).withMinute(minutes)

    val isBeforeNow: Boolean = firstRun.isBefore(now)

    var tmpDateTime = firstRun

    val dateTimeSeekShiftFn: LocalDateTime => LocalDateTime = jobFrequency match {
      case JobFrequency.Daily  => _.plusDays(1)
      case JobFrequency.Hourly => _.plusHours(1)
    }

    Iterator
      .continually({
        tmpDateTime = dateTimeSeekShiftFn(tmpDateTime);
        tmpDateTime
      })
      .takeWhile(localDateTime => localDateTime.isBefore(now))
      .toList
      .lastOption
      .getOrElse(if (isBeforeNow) dateTimeSeekShiftFn(firstRun) else firstRun)
  }
}
