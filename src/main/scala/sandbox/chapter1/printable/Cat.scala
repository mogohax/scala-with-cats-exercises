package sandbox.chapter1.printable

import PrintableInstances._
import PrintableSyntax._

case class Cat(name: String, age: Int, color: String)

object CatPrinter {
  implicit val catPrinter: Printable[Cat] = (cat: Cat) => {
    val nameS = cat.name.fmt
    val ageS = cat.age.fmt
    val colorS = cat.color.fmt

    s"$nameS is a $ageS year-old $colorS cat. Init?"
  }
}
