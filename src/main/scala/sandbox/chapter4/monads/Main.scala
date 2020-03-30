package sandbox.chapter4.monads

/**
 * 4.1.2 Exercise: Getting Func-y
 */
object Main extends App {
  // Every monad is also a functor.
  // We can define map in the same way for every monad using the existing methods, flatMap and pure:
  trait Monad[F[_]] {
    def pure[A](a: A): F[A]

    def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

    // Try defining map yourself now.
    def map[A, B](value: F[A])(func: A => B): F[B] =
      flatMap(value)(func andThen pure)
  }

  val opzhionMonad: Monad[Opzhion] = new Monad[Opzhion] {
    def pure[A](a: A): Opzhion[A] = Opzhion.som(a)

    def flatMap[A, B](value: Opzhion[A])(func: A => Opzhion[B]): Opzhion[B] = {
      value match {
        case Som(value) => func(value)
        case Non => Non
      }
    }
  }

  val timesFive = (x: Int) => x * 5
  val maybeDivideByTwo = (x: Int) => if (x == 0) Opzhion.non else Opzhion.som(x / 2)

  println(opzhionMonad.map(Opzhion.som(5))(timesFive)) // Som(25)
  println(opzhionMonad.map(Opzhion.som(12))(maybeDivideByTwo)) // Som(Som(6))
  println(opzhionMonad.flatMap(Opzhion.som(12))(maybeDivideByTwo)) // Som(6)
  println(opzhionMonad.map(Opzhion.som(0))(maybeDivideByTwo)) // Som(Non)
  println(opzhionMonad.flatMap(Opzhion.som(0))(maybeDivideByTwo)) // Non
  println(opzhionMonad.flatMap(Opzhion.non)(maybeDivideByTwo)) // Non
  println(opzhionMonad.map(Opzhion.non)(timesFive)) // Non

  // Option's retarded cousin Opzhion.
  // We will conduct experiments on him.
  sealed trait Opzhion[+A] {
    def get: A
  }
  final case class Som[+A](value: A) extends Opzhion[A] {
    def get: A = value
  }
  final case object Non extends Opzhion[Nothing] {
    def get = throw new Exception("Nothing to get")
  }
  object Opzhion {
    def som[A](value: A): Opzhion[A] = Som(value)
    def non[A]: Opzhion[A] = Non
  }
}
