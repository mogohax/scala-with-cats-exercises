package sandbox.chapter4.writers

import cats.data.Writer
import cats.syntax.applicative._ // for pure
import cats.instances.vector._ // for Monoid
import cats.syntax.writer._ // for tell

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  type Logged[A] = Writer[Vector[String], A]

  val a = Writer(Vector("msg1", "msg2", "msg3"), 123)
  println(a.value)
  println(a.written)

  val b = 321.writer(Vector("msg4", "msg5", "msg6"))
  val (log, result) = b.run
  println(result)
  println(log)

  val writer1 = for {
    a <- 10.pure[Logged]
    _ <- Vector("a", "b", "c").tell
    b <- 32.writer(Vector("x", "y", "z"))
  } yield a + b
  println(writer1.run)

  val writer2 = writer1.mapWritten(_.map(_.toUpperCase))
  println(writer2.run)

  val writer3 = writer1.bimap(
    log => log.map(_.toUpperCase()),
    res => res * 100
  )
  println(writer3.run)

  val writer4 = writer1.mapBoth({ (log, res) =>
    val log2 = log.map(_ + "!")
    val res2 = res * 1000

    (log2, res2)
  })
  println(writer4.run)

  val writer5 = writer1.reset
  println(writer5.run)

  val writer6 = writer1.swap
  println(writer6.run)

  /**
   * 4.7.3 Exercise: Show Your Working
   *
   * Writers are useful for logging operations in multi-threaded environments.
   * Let’s confirm this by computing (and logging) some factorials.
   *
   * The factorial function below computes a factorial and prints out the intermediate steps as it runs.
   * The slowly helper function ensures this takes a while to run, even on the very small examples below:
   */

  def slowly[A](body: => A) =
    try body finally Thread.sleep(100)

  def factorial(n: Int): Int = {
    val ans = slowly(if(n == 0) 1 else n * factorial(n - 1))
    println(s"fact $n $ans")
    ans
  }
  println(factorial(5))

  // If we start several factorials in parallel, the log messages can become interleaved on standard out.
  // This makes it difficult to see which messages come from which computation:
  val multiFact = Await.result(Future.sequence(Vector(
    Future(factorial(3)),
    Future(factorial(3))
  )), 5.seconds)
  println(multiFact)

  // Rewrite factorial so it captures the log messages in a Writer.
  // Demonstrate that this allows us to reliably separate the logs for concurrent computations.
  def loggedFactorial(n: Int): Logged[Int] = {
    slowly(
      if (n == 0) 1.pure[Logged] else loggedFactorial(n - 1).map(_ * n)
    ).mapBoth((log, ans) => (log :+ s"fact $n $ans", ans))
  }
  println("--- test ---")
  val multiFact2 = Await.result(Future.sequence(Vector(
    Future(loggedFactorial(5).run),
    Future(loggedFactorial(7).run)
  )), 10.seconds)
  println(multiFact2)
}
