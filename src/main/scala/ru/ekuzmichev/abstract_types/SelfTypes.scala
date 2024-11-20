package ru.ekuzmichev.abstract_types

import scala.collection.mutable

trait Database[T] {
  def save(data: T)
}

trait MemoryDatabase[T] extends Database[T] {
  val db: mutable.Seq[T] = mutable.Seq.empty

  override def save(data: T): Unit = {
    System.out.println("Saving to in memory database.")
    db.:+(data)
  }
}

trait FileDatabase[T] extends Database[T] {
  override def save(data: T): Unit = {
    System.out.println("Saving to file.")
  }
}

trait History {
  def add(): Unit = {
    System.out.println("Action added to history.")
  }
}

trait Mystery {
  def add(): Unit = {
    System.out.println("Mystery added!")
  }
}

trait Persister[T] {
  this: Database[T] with History with Mystery =>
  def persist(data: T): Unit = {
    System.out.println("Calling persist.")
    save(data)
    add()
  }
}

// In the preceding code, we included our self type using the statement this: Database[T] =>.
// This allows us to access the methods of our included types directly as if they were methods
// of the trait that includes them. Another way of doing the same here is by writing
// self: Database[T] => instead. There are many examples out there that use the latter approach,
// which is useful to avoid confusion if we need to refer to this in some nested trait or class definitions.
// Calling the methods of the injected dependencies using this approach, however, would require
// the developer to use self. in order to gain access to the required methods.

class FilePersister[T] extends Persister[T] with FileDatabase[T] with History with Mystery {
  // Solution for conflicting members of the same type
  override def add(): Unit = super[History].add()
}
class MemoryPersister[T] extends Persister[T] with MemoryDatabase[T] with History with Mystery {
  // Or without specifying super type and leave it to linearization (last trait (Mystery) method is used)
  override def add(): Unit = super.add()
}

object PersisterExample {
  def main(args: Array[String]): Unit = {
    val fileStringPersister = new FilePersister[String]
    val memoryIntPersister  = new MemoryPersister[Int]

    fileStringPersister.persist("Something")
    fileStringPersister.persist("Something else")

    memoryIntPersister.persist(100)
    memoryIntPersister.persist(123)
  }
}
