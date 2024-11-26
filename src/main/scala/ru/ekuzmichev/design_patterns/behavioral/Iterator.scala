package ru.ekuzmichev.design_patterns.behavioral

import scala.collection.mutable

// The iterator design pattern provides a way to access the elements of
// an aggregate object (collection) in a sequential manner
// without exposing the underlying representation of the items.
object Iterator {
  case class Student(name: String, age: Int)

  class StudentIterator(students: Array[Student]) extends Iterator[Student] {
    var currentPos = 0

    override def hasNext: Boolean = currentPos < students.size

    override def next(): Student = {
      val result = students(currentPos)
      currentPos = currentPos + 1
      result
    }
  }

  // We've created a custom iterator just as an example.
  // However, in reality, you would just implement Iterable in the ClassRoom
  // class and return the iterator of the underlying collection (students, in this case).
  class ClassRoom extends Iterable[Student] {
    val students: mutable.ListBuffer[Student] = mutable.ListBuffer[Student]()

    def add(student: Student): Unit = {
      student +=: students
    }

    override def iterator: Iterator[Student] = new StudentIterator(students.toArray)
  }
}

object IteratorExample {
  import Iterator._

  def main(args: Array[String]): Unit = {
    val classRoom = new ClassRoom
    classRoom.add(Student("Ivan", 26))
    classRoom.add(Student("Maria", 26))
    classRoom.add(Student("John", 25))
    classRoom.foreach(println)
  }
}

