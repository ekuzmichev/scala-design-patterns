package ru.ekuzmichev.design_patterns.creational

// The factory method design pattern exists in order to encapsulate
// an actual class instantiation. It simply provides an interface
// to create an object, and then the subclasses of the factory
// decide which concrete class to instantiate.
//
// This design pattern could become useful in cases where we want
// to create different objects during the runtime of the application.
//
// This design pattern is also helpful when object creation would
// otherwise require extra parameters to be passed by the developer.

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

trait DatabaseClient {
  def executeQuery(query: String): Unit = {
    val connection = connect()
    connection.executeQuery(query)
  }

  // Factory method
  protected def connect(): SimpleConnection
}

class MysqlClient extends DatabaseClient {
  override protected def connect(): SimpleConnection = new SimpleMysqlConnection
}

class PgSqlClient extends DatabaseClient {
  override protected def connect(): SimpleConnection = new SimplePgSqlConnection
}

object Example {
  def main(args: Array[String]): Unit = {
    val clientMySql: DatabaseClient = new MysqlClient
    clientMySql.executeQuery("SELECT * FROM users")

    val clientPgSql: DatabaseClient = new PgSqlClient
    clientPgSql.executeQuery("SELECT * FROM employees")
  }
}
