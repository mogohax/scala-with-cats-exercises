package sandbox.chapter1.typeclass
import JsonWriterInstances._
import JsonSyntax._

object Main extends App {

  println(
    Json.toJson(Person("dave", "dave@dave.dave"))
  )

  println(
    Person("Syntax", "syn@tax.yo").toJson
  )

  println(
    "asda".toJson
  )

  println(implicitly[JsonWriter[String]])

}
