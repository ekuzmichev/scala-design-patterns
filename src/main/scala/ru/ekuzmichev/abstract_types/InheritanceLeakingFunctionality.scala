package ru.ekuzmichev.abstract_types

trait DB {
  def connect(): Unit = {
    System.out.println("Connected.")
  }

  def dropDatabase(): Unit = {
    System.out.println("Dropping!")
  }

  def close(): Unit = {
    System.out.println("Closed.")
  }
}

trait UserDB extends DB {
  def createUser(username: String): Unit = {
    connect()
    try {
      System.out.println(s"Creating a user: $username")
    } finally {
      close()
    }
  }

  def getUser(username: String): Unit = {
    connect()
    try {
      System.out.println(s"Getting a user: $username")
    } finally {
      close()
    }
  }
}

trait UserService extends UserDB {
  // Dangerous method has been leaked via inheritance to UserService
  def bad(): Unit = {
    dropDatabase()
  }
}

// Solution: Self-types

{
  trait UserDB {
    this: DB =>

    def createUser(username: String): Unit = {
      connect()
      try {
        System.out.println(s"Creating a user: $username")
      } finally {
        close()
      }
    }

    def getUser(username: String): Unit = {
      connect()
      try {
        System.out.println(s"Getting a user: $username")
      } finally {
        close()
      }
    }
  }

  trait UserService {
    this: UserDB =>

    // does not compile
    // def bad(): Unit = {
    //  dropDatabase()
    // }
  }
}
