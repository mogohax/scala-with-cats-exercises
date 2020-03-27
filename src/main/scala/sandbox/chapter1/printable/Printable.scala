package sandbox.chapter1.printable

trait Printable[A] {
  def fmt(value: A): String
}

//object Printable {
//  def fmt[A](input: A)(implicit valuePrinter: Printable[A]): String = valuePrinter.fmt(input)
//
//  def print[A](input: A)(implicit valuePrinter: Printable[A]): Unit = println(valuePrinter.fmt(input))
//}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    def fmt(implicit printable: Printable[A]): String =
      printable.fmt(value)

    def print(implicit printable: Printable[A]): Unit =
      println(fmt(printable))
  }
}

