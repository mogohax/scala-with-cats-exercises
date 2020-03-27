package sandbox.chapter1.cats

import cats._
import cats.implicits._

import java.util.Date
import ShowInstances._

object Main extends App {
  val showInt = 123.show
  val showString = "abc".show

  implicit val dateShow: Show[Date] = new Show[Date] {
    def show(date: Date): String = s"${date.getTime}ms since epoch."
  }

  println(new Date().show)


  val cat = Cat("Murke", 7, None)
  println(cat.show)

  val cat2 = Cat("Baterija", 2, Some(Person("Kazys")))
  println(cat2.show)
}
