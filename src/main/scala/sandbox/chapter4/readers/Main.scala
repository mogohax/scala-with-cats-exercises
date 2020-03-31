package sandbox.chapter4.readers

import cats.data.Reader
import cats.syntax.applicative._

object Main extends App {
  case class Cat(name: String, favouriteFood: String)

  val catName: Reader[Cat, String] = Reader(cat => cat.name)
  println(catName.run(Cat("Garfield", "lasagna")))

  val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello, ${name}")
  println(greetKitty.run(Cat("Murke", "Morka")))

  val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favouriteFood}")
  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet $feed"
  println(greetAndFeed(Cat("Garfield", "Lasagne")))

  /**
   * 4.8.3 Exercise: Hacking on Readers
   *
   * The classic use of Readers is to build programs that accept a configuration as a parameter.
   * Let’s ground this with a complete example of a simple login system.
   * Our configuration will consist of two databases: a list of valid users and a list of their passwords:
   */
  final case class Db(usernames: Map[Int, String], passwords: Map[String, String])

  /**
   * Start by creating a type alias DbReader for a Reader that consumes a Db as input.
   * This will make the rest of our code shorter.
   */
  type DbReader[A] = Reader[Db, A]

  /**
   * Now create methods that generate DbReaders to look up the username for an Int user ID,
   *    and look up the password for a String username.
   * The type signatures should be as follows:
   */
  def findUsername(userId: Int): DbReader[Option[String]] = Reader(db => db.usernames.get(userId))
  def checkPassword(username: String, password: String): DbReader[Boolean] = Reader(db =>
    db.passwords.get(username) match {
    case Some(userPassword) => userPassword == password
    case _ => false
  })

  /**
   * Finally create a checkLogin method to check the password for a given user ID.
   * The type signature should be as follows:
   */
  def checkLogin( userId: Int, password: String): DbReader[Boolean] =
    for {
      maybeUsername <- findUsername(userId)
      loginSuccess <- maybeUsername.map(username => {
        checkPassword(username, password)
      }).getOrElse(false.pure[DbReader])
    } yield loginSuccess

  /**
   * You should be able to use checkLogin as follows:
   */
  val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo"
  )

  val passwords = Map(
    "dade"  -> "zerocool",
    "kate"  -> "acidburn",
    "margo" -> "secret"
  )

  val db = Db(users, passwords)

  println(checkLogin(1, "zerocool").run(db)) // true
  println(checkLogin(4, "davinci").run(db)) // false
}
