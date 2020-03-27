package sandbox.chapter1.cats

import cats._
import cats.implicits._

object ShowInstances {

  implicit val personShow: Show[Person] = (person: Person) => {
    val name = person.name.show
    s"Human $name"
  }

//  implicit val optionShow: Show[Option[]]

  implicit val catShow: Show[Cat] = (cat: Cat) => {
    val name = cat.name.show
    val age = cat.age.show
    val favPerson = cat.favPerson match {
      case Some(person) => person.show
      case None => "---"
    }
    s"Kitty named $name, $age years old. Fav person: $favPerson"
  }

}
