package ru.ekuzmichev.complete_application

import com.typesafe.scalalogging.LazyLogging
import org.json4s._
import org.json4s.jackson.JsonMethods._

import java.io.File

trait JobConfigReaderServiceComponent {
  this: AppConfigComponent with IoServiceComponent =>

  val jobConfigReaderService: JobConfigReaderService

  class JobConfigReaderService extends LazyLogging {
    private val customSerializers = List(JobFrequencySerializer, JobTypeSerializer)

    private implicit val formats: Formats = DefaultFormats ++ customSerializers + JobConfig.jobConfigFieldSerializer

    def readJobConfigs(): List[JobConfig] =
      ioService
        .getAllFilesWithExtension(
          appConfigService.configPath,
          appConfigService.configExtension
        )
        .flatMap { path: String =>
          try {
            val config = parse(new File(path)).extract[JobConfig]
            Some(config)
          } catch {
            case t: Throwable =>
              logger.error(s"Error reading job config from file at path=$path", t)
              None
          }
        }
  }
}
