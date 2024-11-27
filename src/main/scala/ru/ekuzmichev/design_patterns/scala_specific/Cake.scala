package ru.ekuzmichev.design_patterns.scala_specific

import org.h2.jdbcx.JdbcConnectionPool

import java.sql.{Connection, PreparedStatement, ResultSet}
import javax.sql.DataSource

// It is used for dependency injection
//
object Cake {
  case class Class(id: Int, name: String)
  case class Person(id: Int, name: String, age: Int)

  trait DatabaseService {
    val dbDriver: String
    val connectionString: String
    val username: String
    val password: String

    val dataSource: DataSource = JdbcConnectionPool.create(connectionString, username, password)

    def getConnection: Connection = dataSource.getConnection
  }

  trait DatabaseComponent {
    val databaseService: DatabaseService

    class H2DatabaseService(val connectionString: String, val username: String, val password: String)
        extends DatabaseService {
      val dbDriver = "org.h2.Driver"
    }
  }

  trait MigrationComponent {
    this: DatabaseComponent =>

    val migrationService: MigrationService

    class MigrationService() {
      def runMigrations(): Unit = {
        val connection = databaseService.getConnection
        try {
          // create the database
          createPeopleTable(connection)
          createClassesTable(connection)
          createPeopleToClassesTable(connection)
          // populate
          insertPeople(
            connection,
            List(
              Person(1, "Ivan", 26),
              Person(2, "Maria", 25),
              Person(3, "John", 27)
            )
          )
          insertClasses(
            connection,
            List(
              Class(1, "Scala Design Patterns"),
              Class(2, "Java Programming"),
              Class(3, "Mountain Biking")
            )
          )
          signPeopleToClasses(
            connection,
            List(
              (1, 1),
              (1, 2),
              (1, 3),
              (2, 1),
              (3, 1),
              (3, 3)
            )
          )
        } finally {
          connection.close()
        }
      }

      private def createPeopleTable(connection: Connection): Unit = {
        val statement = connection.prepareStatement(
          """
            |CREATE TABLE people(
            | id INT PRIMARY KEY,
            | name VARCHAR(255) NOT NULL,
            | age INT NOT NULL 
            |)
        """.stripMargin
        )
        try {
          statement.executeUpdate()
        } finally {
          statement.close()
        }
      }

      private def createClassesTable(connection: Connection): Unit = {
        val statement = connection.prepareStatement(
          """
            |CREATE TABLE classes(
            | id INT PRIMARY KEY,
            | name VARCHAR(255) NOT NULL
            |)
        """.stripMargin
        )
        try {
          statement.executeUpdate()
        } finally {
          statement.close()
        }
      }

      private def createPeopleToClassesTable(connection: Connection): Unit = {
        val statement = connection.prepareStatement(
          """
            |CREATE TABLE people_classes(
            | person_id INT NOT NULL,
            | class_id INT NOT NULL,
            | PRIMARY KEY(person_id, class_id),
            | FOREIGN KEY(person_id) REFERENCES people(id) ON DELETE CASCADE ON UPDATE CASCADE,
            | FOREIGN KEY(class_id) REFERENCES classes(id) ON DELETE CASCADE ON UPDATE CASCADE
            |)
        """.stripMargin
        )
        try {
          statement.executeUpdate()
        } finally {
          statement.close()
        }
      }

      private def insertPeople(connection: Connection, people: List[Person]): Unit = {
        val statement = connection.prepareStatement(
          "INSERT INTO people(id, name, age) VALUES (?, ?, ?)"
        )
        try {
          people.foreach { case person =>
            statement.setInt(1, person.id)
            statement.setString(2, person.name)
            statement.setInt(3, person.age)
            statement.addBatch()
          }
          statement.executeBatch()
        } finally {
          statement.close()
        }
      }

      private def insertClasses(connection: Connection, classes: List[Class]): Unit = {
        val statement = connection.prepareStatement(
          "INSERT INTO classes(id, name) VALUES (?, ?)"
        )
        try {
          classes.foreach { case cls =>
            statement.setInt(1, cls.id)
            statement.setString(2, cls.name)
            statement.addBatch()
          }
          statement.executeBatch()
        } finally {
          statement.close()
        }
      }

      private def signPeopleToClasses(connection: Connection, peopleToClasses: List[(Int, Int)]): Unit = {
        val statement = connection.prepareStatement(
          "INSERT INTO people_classes(person_id, class_id) VALUES (?, ?)"
        )
        try {
          peopleToClasses.foreach { case (personId, classId) =>
            statement.setInt(1, personId)
            statement.setInt(2, classId)
            statement.addBatch()
          }
          statement.executeBatch()
        } finally {
          statement.close()
        }
      }
    }
  }

  trait DaoComponent {
    this: DatabaseComponent =>

    val dao: Dao

    class Dao() {
      def getPeople: List[Person] = {
        val connection = databaseService.getConnection
        try {
          executeSelect(
            connection.prepareStatement("SELECT id, name, age FROM people")
          ) { rs =>
            readResultSet(rs) { row =>
              Person(row.getInt(1), row.getString(2), row.getInt(3))
            }
          }
        } finally {
          connection.close()
        }
      }

      def getClasses: List[Class] = {
        val connection = databaseService.getConnection
        try {
          executeSelect(
            connection.prepareStatement("SELECT id, name FROM classes")
          ) { rs =>
            readResultSet(rs) { row =>
              Class(row.getInt(1), row.getString(2))
            }
          }
        } finally {
          connection.close()
        }
      }

      def getPeopleInClass(className: String): List[Person] = {
        val connection = databaseService.getConnection
        try {
          val statement = connection.prepareStatement(
            """
              |SELECT p.id, p.name, p.age 
              |FROM people p 
              | JOIN people_classes pc ON p.id = pc.person_id
              | JOIN classes c ON c.id = pc.class_id
              |WHERE c.name = ? 
          """.stripMargin
          )
          statement.setString(1, className)
          executeSelect(
            statement
          ) { rs =>
            readResultSet(rs) { row =>
              Person(row.getInt(1), row.getString(2), row.getInt(3))
            }
          }
        } finally {
          connection.close()
        }

      }

      private def executeSelect[T](preparedStatement: PreparedStatement)(f: ResultSet => List[T]): List[T] =
        try {
          f(preparedStatement.executeQuery())
        } finally {
          preparedStatement.close()
        }

      private def readResultSet[T](rs: ResultSet)(f: ResultSet => T): List[T] =
        Iterator
          .continually((rs.next(), rs))
          .takeWhile(_._1)
          .map { case (_, row) =>
            f(rs)
          }
          .toList

    }
  }

  trait UserComponent {
    this: DaoComponent =>

    val userService: UserService
    class UserService {
      def getAverageAgeOfUsersInClass(className: String): Double = {
        val (ageSum, peopleCount) = dao.getPeopleInClass(className).foldLeft((0, 0)) { case ((sum, count), person) =>
          (sum + person.age, count + 1)
        }
        if (peopleCount != 0) {
          ageSum.toDouble / peopleCount.toDouble
        } else {
          0.0
        }
      }
    }
  }

  object ApplicationComponentRegistry
      extends UserComponent
      with DaoComponent
      with DatabaseComponent
      with MigrationComponent {

    override val dao: ApplicationComponentRegistry.Dao = new Dao
    override val databaseService: DatabaseService = new H2DatabaseService("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "")
    override val migrationService: ApplicationComponentRegistry.MigrationService = new MigrationService
    override val userService: ApplicationComponentRegistry.UserService           = new UserService
  }
}

object CakeExample {
  import Cake._
  import ApplicationComponentRegistry._

  def main(args: Array[String]): Unit = {
    migrationService.runMigrations()
    println(dao.getPeople)
    println(dao.getClasses)
    println(dao.getPeopleInClass("Scala Design Patterns"))
    println(dao.getPeopleInClass("Mountain Biking"))
    println(
      s"Average age of everyone in Scala Design Patterns: ${userService.getAverageAgeOfUsersInClass("Scala Design Patterns")}"
    )
  }
}
