package ru.ekuzmichev.design_patterns.creational

// The purpose is the same as all factory design patterns—to encapsulate the object creation logic
// and hide it from the user. The difference is how it is implemented.
//
//The abstract factory design pattern relies on object composition in contrast to inheritance,
// which is used by the factory method. Here, we have a separate object, which provides
// an interface to create instances of the classes we need.

object AbstractFactory {
  trait SimpleConnection {
    def getName(): String

    def executeQuery(query: String): Unit
  }

  class SimpleMysqlConnection extends SimpleConnection {
    override def getName(): String = "SimpleMysqlConnection"

    override def executeQuery(query: String): Unit = {
      System.out.println(s"Executing the query '$query' the MySQL way.")
    }
  }

  class SimplePgSqlConnection extends SimpleConnection {
    override def getName(): String = "SimplePgSqlConnection"

    override def executeQuery(query: String): Unit = {
      System.out.println(s"Executing the query '$query' the PgSQL way.")
    }
  }

  trait DatabaseConnectorFactory {
    def connect(): SimpleConnection
  }

  class MySqlFactory extends DatabaseConnectorFactory {
    override def connect(): SimpleConnection = new SimpleMysqlConnection
  }

  class PgSqlFactory extends DatabaseConnectorFactory {
    override def connect(): SimpleConnection = new SimplePgSqlConnection
  }

  class DatabaseClient(connectorFactory: DatabaseConnectorFactory) {
    def executeQuery(query: String): Unit = {
      val connection = connectorFactory.connect()
      connection.executeQuery(query)
    }
  }
}

object AbstractFactoryExample {
  import AbstractFactory._

  def main(args: Array[String]): Unit = {
    val clientMySql: DatabaseClient = new DatabaseClient(new MySqlFactory)
    val clientPgSql: DatabaseClient = new DatabaseClient(new PgSqlFactory)
    clientMySql.executeQuery("SELECT * FROM users")
    clientPgSql.executeQuery("SELECT * FROM employees")
  }
}

// What it is not so good for?
//
// It could be problematic if the objects and methods we are using (SimpleConnection, in our case) change signatures.
// In some cases, this pattern could also complicate our code unnecessarily and make it unreadable and hard to follow.