package sandbox.chapter1.equality

import cats.Eq
import cats.instances.int._
import cats.instances.option._
import cats.instances.string._
import cats.syntax.eq._
import cats.syntax.option._

import java.time.LocalDate

object Main  extends App {
  println("--- 1.5.2 compare ints ---")
  val intComparer = Eq[Int]

  println(intComparer.eqv(123, 123))
  println(123 === 123)

  println(intComparer.neqv(123, 321))
  println(123 =!= 321)

  // Compile errors
//  intComparer.eqv(123, "123")
//  123 === "123"

  ////////////////////
  println("--- 1.5.3 compare options ---")

  // Also an error
//  Some(1) === None
  println((Some(1): Option[Int]) === (None: Option[Int]))
  println(Option(1) === Option.empty)

  // special cats syntax for the above
  1.some === none[Int]
  1.some =!= none[Int]

  /////////////////////
  println("--- 1.5.4 compare custom types ---")

  implicit val dateEq: Eq[LocalDate] = (date1, date2) => date1.isEqual(date2)

  val date1 = LocalDate.now()
  val date2 = LocalDate.now().plusMonths(1)

  println(date1 === date1)
  println(date1 === date2)

  /////////////////////
  println("--- 1.5.5 Exercise: Equality, Liberty, and Felinity ---")
  /**
   * Implement an instance of Eq for our running Cat example:
   */
  final case class Cat(name: String, age: Int, color: String)

  // Use this to compare the following pairs of objects for equality and inequality:
  val cat1 = Cat("Garfield",   38, "orange and black")
  val cat2 = Cat("Heathcliff", 33, "orange and black")
  val optionCat1 = Option(cat1)
  val optionCat2 = Option.empty[Cat]

  // Solution

  implicit val catEq: Eq[Cat] = (cat1: Cat, cat2: Cat) => {
    (cat1.name === cat2.name) && (cat1.age === cat2.age) && (cat1.color === cat2.color)
  }

  println(cat1 === cat1)
  println(cat1 === cat2)

  println(optionCat1 === optionCat1)
  println(optionCat1 === optionCat2)
}
