package sandbox.chapter1.printable

object PrintableInstances {
//  implicit val stringPrinter: Printable[String] = new Printable[String] {
//    def fmt(input: String) = input.toUpperCase.reverse
//  }
//
//  implicit val intPrinter: Printable[Int] = new Printable[Int] {
//    def fmt(input: Int) = input.toString
//  }

  implicit val stringPrinter: Printable[String] = (value: String) => value.reverse.toUpperCase

  implicit val intPrinter: Printable[Int] = (value: Int) => value.toString
}
