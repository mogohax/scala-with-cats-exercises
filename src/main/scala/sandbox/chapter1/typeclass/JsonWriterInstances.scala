package sandbox.chapter1.typeclass

object JsonWriterInstances {

  implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
    def write(value: String): Json = JsString(value)
  }

  implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
    override def write(value: Person): Json = JsObject(Map(
      "name" -> JsString(value.name),
      "email" -> JsString(value.email)
    ))
  }

//  implicit val uppercasePersonWriter: JsonWriter[Person] = new JsonWriter[Person] {
//    override def write(value: Person): Json = JsObject(Map(
//      "name" -> JsString(value.name.toUpperCase),
//      "email" -> JsString(value.email.toUpperCase)
//    ))
//  }

}
