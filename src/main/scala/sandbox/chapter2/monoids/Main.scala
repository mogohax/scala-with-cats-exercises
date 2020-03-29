package sandbox.chapter2.monoids

import cats.Monoid
import cats.syntax.semigroup._ // for |+|

object Main extends App {

  def add[A](items: List[A])(implicit monoid: Monoid[A]): A =
    items.foldLeft(monoid.empty)(_ |+| _)

  // SuperAdder is entering the POS (point-of-sale, not the other POS) market.
  // Now we want to add up Orders:
  case class Order(totalCost: Double, quantity: Double)
  val orders = List(Order(10, 5), Order(15, 7.4), Order(22.5, 9.1))

  // We need to release this code really soon so we can’t make any modifications to add. Make it so!
  implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
    override def combine(order1: Order, order2: Order): Order = Order(
      order1.totalCost + order2.totalCost, order1.quantity + order2.quantity
    )
    override def empty: Order = Order(0, 0)
  }

  println(add(orders))
}
