package sandbox.chapter3.functors

import cats.Functor
import cats.syntax.functor._ // for map

/**
 * 3.5.4 Exercise: Branching out with Functors
 */
object Main extends App {
  //Write a Functor for the following binary tree data type.
  // Verify that the code works as expected on instances of Branch and Leaf:

  sealed trait Tree[+A]

  final case class Branch[A](left: Tree[A], right: Tree[A])
    extends Tree[A]

  final case class Leaf[A](value: A) extends Tree[A]

  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    def map[A, B](value: Tree[A])(func: A => B): Tree[B] = {
      value match {
        case Leaf(leaf) => Leaf(func(leaf))
        case Branch(left, right) => Branch(left.map(func), right.map(func))
      }
    }
  }

  val intTree: Tree[Int] = Branch(Leaf(1), Branch(Leaf(2), Leaf(3)))
  println(intTree)

  val strMap = intTree.map(x => s"${x*2}!")
  println(strMap)

  object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

    def leaf[A](value: A): Tree[A] = Leaf(value)
  }

  // Boom! Explodes!
//  Branch(Leaf(10), Leaf(20)).map(_ * 2)

  // Boom! Works!
  println(Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2))
}
