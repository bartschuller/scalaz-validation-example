package typeclasses

trait Query[R]

trait Ordering[E,R] {
  def computeOrder(order: String): String
}

trait DAOSupport[E] {
  def sQ[R](q: Query[R], order: String)(implicit ordering: Ordering[E,R]) {
    println("Ordering: "+ordering.computeOrder(order))
  }
}

class Person

object PersonDAO extends DAOSupport[Person]

object PersonOrderings {
  implicit val personStringOrdering = new Ordering[Person,String] {
    def computeOrder(order: String) = {
      "class Person, fetching a String, ordered by "+order
    }
  }

  implicit val personPersonOrdering = new Ordering[Person,Person] {
    def computeOrder(order: String) = {
      "class Person, fetching a Person, ordered by "+order
    }
  }
}

object UserCode {
  import PersonOrderings._

  def main(args: Array[String]) {
    val baseQ = new Query[String] {}
    PersonDAO.sQ(baseQ, "name")
    val baseQ2 = new Query[Person] {}
    PersonDAO.sQ(baseQ2, "name")
  }
}