package ru.ekuzmichev.abstract_types

// Generics and abstract types can be interchangeable.
// We might have to do some extra work, but in the end,
// we could get what the abstract types provide using generics.
//
// Which one is chosen depends on different factors, some of which are
// personal preferences, such as whether someone is aiming for readability
// or a different kind of usage of the classes.

// Use generics:
//
//If you need just type instantiation; a good example is the standard collection classes
//If you are creating a family of types
//
// Use abstract types:
//
//If you want to allow people to mix in types using traits
//If you need better readability in scenarios where both could be interchangeable
//If you want to hide the type definition from the client code

abstract class PrintData
abstract class PrintMaterial
abstract class PrintMedia

trait Printer {
  type Data <: PrintData
  type Material <: PrintMaterial
  type Media <: PrintMedia

  def print(data: Data, material: Material, media: Media) =
    s"Printing $data with $material material on $media media."
}
case class Paper() extends PrintMedia
case class Air()   extends PrintMedia

case class Text()  extends PrintData
case class Model() extends PrintData

case class Toner()   extends PrintMaterial
case class Plastic() extends PrintMaterial

class LaserPrinter extends Printer {
  type Media    = Paper
  type Data     = Text
  type Material = Toner
}

class ThreeDPrinter extends Printer {
  type Media    = Air
  type Data     = Model
  type Material = Plastic
}

object PrinterExample {
  def main(args: Array[String]): Unit = {
    val laser  = new LaserPrinter
    val threeD = new ThreeDPrinter

    System.out.println(laser.print(Text(), Toner(), Paper()))
    System.out.println(threeD.print(Model(), Plastic(), Air()))
  }
}

trait GenericPrinter[Data <: PrintData, Material <: PrintMaterial, Media <: PrintMedia] {
  def print(data: Data, material: Material, media: Media) =
    s"Printing $data with $material material on $media media."
}

class GenericLaserPrinter[Data <: Text, Material <: Toner, Media <: Paper] extends GenericPrinter[Data, Material, Media]
class GenericThreeDPrinter[Data <: Model, Material <: Plastic, Media <: Air]
    extends GenericPrinter[Data, Material, Media]

object GenericPrinterExample {
  def main(args: Array[String]): Unit = {
    val genericLaser  = new GenericLaserPrinter[Text, Toner, Paper]
    val genericThreeD = new GenericThreeDPrinter[Model, Plastic, Air]
    System.out.println(genericLaser.print(Text(), Toner(), Paper()))
    System.out.println(genericThreeD.print(Model(), Plastic(), Air()))
  }
}
