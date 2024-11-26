package ru.ekuzmichev.design_patterns.behavioral

// The visitor design pattern helps us add new operations to existing object structures without modifying them.
//
// This helps us to design our structures separately and
// then use the visitor design pattern to add functionality on top.
//
// Another case where the visitor design pattern could be useful
// is if we are building a big object structure with many different types
// of nodes that support different operations. Instead of creating a base node
// that has all the operations and only a few of them are implemented by
// the concrete nodes or use type casting, we could create visitors that
// will add the functionality we need where we need it.
//
// The visitor design pattern is perfect for applications that have
// large object hierarchies, where adding a new functionality
// will involve a lot of refactoring.
// Whenever we need to be able to do multiple different things with
// an object hierarchy and when changing the object classes
// could be problematic, the visitor design pattern is a useful alternative.
//
object Visitor {
  object Classic {
    abstract class Element(val text: String) {
      def accept(visitor: Visitor): Unit
    }

    class Title(text: String) extends Element(text) {
      override def accept(visitor: Visitor): Unit =
        visitor.visit(this)
    }

    class Text(text: String) extends Element(text) {
      override def accept(visitor: Visitor): Unit =
        visitor.visit(this)
    }

    class Hyperlink(text: String, val url: String) extends Element(text) {
      override def accept(visitor: Visitor): Unit =
        visitor.visit(this)
    }

    class Document(elements: List[Element]) {
      def accept(visitor: Visitor): Unit =
        elements.foreach(element => element.accept(visitor))
    }

    trait Visitor {
      def visit(title: Title): Unit

      def visit(text: Text): Unit

      def visit(hyperlink: Hyperlink): Unit
    }

    class HtmlExporterVisitor extends Visitor {
      val line: String = java.lang.System.lineSeparator()

      val builder = new StringBuilder

      def getHtml(): String = builder.toString

      override def visit(title: Title): Unit =
        builder.append(s"<h1>${title.text}</h1>").append(line)

      override def visit(text: Text): Unit =
        builder.append(s"<p>${text.text}</p>").append(line)

      override def visit(hyperlink: Hyperlink): Unit =
        builder.append(s"""<a href=\"${hyperlink.url}\">${hyperlink.text}</a>""").append(line)
    }

    class PlainTextExporterVisitor extends Visitor {
      val line: String = java.lang.System.lineSeparator()

      val builder = new StringBuilder

      def getText(): String = builder.toString

      override def visit(title: Title): Unit =
        builder.append(title.text).append(line)

      override def visit(text: Text): Unit =
        builder.append(text.text).append(line)

      override def visit(hyperlink: Hyperlink): Unit =
        builder.append(s"${hyperlink.text} (${hyperlink.url})").append(line)
    }
  }

  // Pass functions to the accept method
  // Use pattern matching instead of having multiple different visit methods
  object ScalaWay {
    abstract class Element(text: String) {
      def accept(visitor: Element => Unit): Unit = visitor(this)
    }

    case class Title(text: String) extends Element(text)

    case class Text(text: String) extends Element(text)

    case class Hyperlink(text: String, val url: String) extends Element(text)

    case class Document(elements: List[Element]) {
      def accept(visitor: Element => Unit): Unit =
        elements.foreach(element => element.accept(visitor))
    }

    object Visitors {
      val line: String = java.lang.System.lineSeparator()

      def htmlExporterVisitor(builder: StringBuilder): Element => Unit = {
        case Title(text) =>
          builder.append(s"<h1>$text</h1>").append(line)
        case Text(text) =>
          builder.append(s"<p>$text</p>").append(line)
        case Hyperlink(text, url) => builder.append(s"""<a href=\"$url\">$text</a>""").append(line)
      }

      def plainTextExporterVisitor(builder: StringBuilder): Element => Unit = {
        case Title(text)          => builder.append(text).append(line)
        case Text(text)           => builder.append(text).append(line)
        case Hyperlink(text, url) => builder.append(s"$text ($url)").append(line)
      }
    }
  }
}

object VisitorClassicExample {

  import Visitor.Classic._

  def main(args: Array[String]): Unit = {
    val document = new Document(
      List(
        new Title("The Visitor Pattern Example"),
        new Text("The visitor pattern helps us add extra functionality without changing the classes."),
        new Hyperlink("Go check it online!", "https://www.google.com/"),
        new Text("Thanks!")
      )
    )

    val htmlExporter      = new HtmlExporterVisitor
    val plainTextExporter = new PlainTextExporterVisitor

    println(s"Export to html:")
    document.accept(htmlExporter)

    println(htmlExporter.getHtml())
    println(s"Export to plain:")

    document.accept(plainTextExporter)
    println(plainTextExporter.getText())
  }
}

object VisitorScalaWayExample {
  import Visitor.ScalaWay._

  def main(args: Array[String]): Unit = {
    val document = new Document(
      List(
        Title("The Visitor Pattern Example"),
        Text("The visitor pattern helps us add extra functionality without changing the classes."),
        Hyperlink("Go check it online!", "https://www.google.com/"),
        Text("Thanks!")
      )
    )

    val html  = new StringBuilder
    val plain = new StringBuilder

    println(s"Export to html:")
    document.accept(Visitors.htmlExporterVisitor(html))

    println(html.toString())
    println(s"Export to plain:")

    document.accept(Visitors.plainTextExporterVisitor(plain))
    println(plain.toString())
  }
}
