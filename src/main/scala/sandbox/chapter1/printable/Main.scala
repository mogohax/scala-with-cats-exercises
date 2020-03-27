package sandbox.chapter1.printable

import CatPrinter._
import PrintableSyntax._
import PrintableInstances._


object Main extends App {
  val cat = Cat("Murke", 5, "Juoda")

  // print the cat
  cat.print

  "string".print

  7.print
}
