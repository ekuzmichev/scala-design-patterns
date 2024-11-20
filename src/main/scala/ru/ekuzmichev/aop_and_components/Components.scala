package ru.ekuzmichev.aop_and_components

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class Food(name: String)

trait Time {
  def getTime(): String
}

trait RecipeFinder {
  def findRecipe(dish: String): String
}

trait Cooker {
  def cook(what: String): Food
}

trait TimeComponent {
  val time: Time

  class TimeImpl extends Time {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    override def getTime(): String = s"The time is: ${LocalDateTime.now().format(formatter)}"
  }
}

trait RecipeComponent {
  val recipe: RecipeFinder

  class RecipeFinderImpl extends RecipeFinder {
    override def findRecipe(dish: String): String = dish match {
      case "chips"    => "Fry the potatoes for 10 minutes."
      case "fish"     => "Clean the fish and put in the oven for 30 minutes."
      case "sandwich" => "Put butter, ham and cheese on the bread, toast and add tomatoes."
      case _          => throw new RuntimeException(s"${dish} is unknown recipe.")
    }
  }
}

trait CookingComponent {
  this: RecipeComponent =>

  val cooker: Cooker

  class CookerImpl extends Cooker {
    override def cook(what: String): Food = {
      val recipeText = recipe.findRecipe(what)
      Food(s"We just cooked $what using the following recipe: '$recipeText'.")
    }
  }
}

class RobotRegistry extends TimeComponent with RecipeComponent with CookingComponent {
  override val time: Time           = new TimeImpl
  override val recipe: RecipeFinder = new RecipeFinderImpl
  override val cooker: Cooker       = new CookerImpl
}

class Robot extends RobotRegistry {
  def cook(what: String) = cooker.cook(what)
  def getTime() = time.getTime()
}

object RobotExample {
  def main(args: Array[String]): Unit = {
    val robot = new Robot
    System.out.println(robot.getTime())
    System.out.println(robot.cook("chips"))
    System.out.println(robot.cook("sandwich"))
  }
}