package ru.ekuzmichev.design_patterns.behavioral

import scala.collection.mutable

// The purpose of the memento design pattern is to provide the ability
// to execute an undo action in order to restore an object to a previous state.
//
// The original memento design pattern is implemented with the help of three main objects:
//
// - Originator: The object whose state we want to be able to restore
// - Caretaker: The object that triggers the changes to the originator object
//    and uses the memento objects for rollback, if needed
// - Memento: The object that carries the actual state of the originator and
//    can be used to restore to one of the previous states
//
// It is important to know that the memento object can be handled only by the originator.
// The caretaker and all other classes can just store it and nothing else.
//
object Memento {
  trait Memento[T] {
    protected val state: T

    def getState(): T = state
  }

  trait Caretaker[T] {
    val states: mutable.Stack[Memento[T]] = mutable.Stack[Memento[T]]()
  }

  trait Originator[T] {
    def createMemento: Memento[T]
    def restore(memento: Memento[T]): Unit
  }

  class TextEditor extends Originator[String] {
    private var builder: StringBuilder = new StringBuilder

    def append(text: String): Unit =
      builder.append(text)

    def delete(): Unit =
      if (builder.nonEmpty) builder.deleteCharAt(builder.length - 1)

    override def createMemento: Memento[String] = new TextEditorMemento(builder.toString)

    override def restore(memento: Memento[String]): Unit =
      this.builder = new StringBuilder(memento.getState())

    def text(): String = builder.toString

    private class TextEditorMemento(val state: String) extends Memento[String]
  }

  class TextEditorManipulator extends Caretaker[String] {
    private val textEditor = new TextEditor

    def save(): Unit =
      states.push(textEditor.createMemento)

    def undo(): Unit =
      if (states.nonEmpty) textEditor.restore(states.pop())

    def append(text: String): Unit = {
      save()
      textEditor.append(text)
    }

    def delete(): Unit = {
      save()
      textEditor.delete()
    }

    def readText(): String = textEditor.text()
  }
}

object MementoExample {
  import Memento._

  def main(args: Array[String]): Unit = {
    val textEditorManipulator = new TextEditorManipulator
    textEditorManipulator.append("This is a chapter about memento.")
    println(s"The text is: '${textEditorManipulator.readText()}'")
    // delete 2 characters
    println("Deleting 2 characters...")
    textEditorManipulator.delete()
    textEditorManipulator.delete()
    // see the text
    println(s"The text is: '${textEditorManipulator.readText()}'")
    // undo
    println("Undoing...")
    textEditorManipulator.undo()
    println(s"The text is: '${textEditorManipulator.readText()}'")
    // undo again
    println("Undoing...")
    textEditorManipulator.undo()
    println(s"The text is: '${textEditorManipulator.readText()}'")
  }
}
