package ru.ekuzmichev.complete_application

import java.io.File

trait IoServiceComponent {
  val ioService: IoService

  class IoService {
    def getAllFilesWithExtension(basePath: String, extension: String): List[String] = {
      val dir: File = new File(basePath)

      if (dir.exists() && dir.isDirectory)
        dir
          .listFiles()
          .filter(isFileWithExtension(extension))
          .map(_.getAbsolutePath)
          .toList
      else
        Nil
    }

    private def isFileWithExtension(extension: String)(file: File) =
      file.isFile && file.getPath.toLowerCase.endsWith(s".$extension")
  }

}
