package ru.ekuzmichev.traits

abstract class Connector {
  def connect()
  def close()
}

// Traits can extend classes
trait ConnectorWithHelper extends Connector {
  def findDriver(): Unit = {
    System.out.println("Find driver called.")
  }
}

class PgSqlConnector extends ConnectorWithHelper {
  override def connect(): Unit = {
    System.out.println("Connected...")
  }

  override def close(): Unit = {
    System.out.println("Closed...")
  }
}