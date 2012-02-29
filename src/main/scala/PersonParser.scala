import java.text.{ParsePosition, SimpleDateFormat}
import java.util.Date
import scalaz._
import Scalaz._

object PersonParser {
  def main(args: Array[String]) {
    tryParse(bad)
    tryParse(partial)
    tryParse(good)
  }

  def tryParse(s: String) {
    println("Trying to parse: "+s)
    parsePerson(s) match {
      case Success(p) => {
        println("  Succesfully parsed a person: " + p)
      }
      case Failure(f) => {
        println("  Parsing failed, with the following errors:")
        f foreach { error => println("    "+error) }
      }
    }
    println
  }

  def parsePerson(in: String): ValidationNEL[String, Person] = {
    val components = in.split(';').lift
    val name = components(0).fold(_.successNel[String], "No name found".failNel[String])
    val date = components(1).fold(parseDate(_), "No date found".failNel[Date])
    val address = components(2).fold(parseAddress(_), "No address found".failNel[List[String]])
    (name |@| date |@| address) { Person(_, _, _)}
  }

  def parseDate(in: String): ValidationNEL[String, Date] = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    sdf.parse(in, new ParsePosition(0)) match {
      case null => ("Can't parse ["+in+"] as a date").failNel[Date]
      case date => date.successNel[String]
    }
  }

  def parseAddress(in: String): ValidationNEL[String,List[String]] = {
    val address = in.split(",\\s*")
    if (address.length > 1)
      address.toList.successNel[String]
    else
      ("An address needs to have a street and a city, separated by a comma; found ["+in+"]").failNel[List[String]]
  }

  val bad = "Name Only"
  val partial = "Joe Colleague;1974-??-??;Rotterdam"
  val good = "Bart Schuller;2012-02-29;Some Street 123, Some Town"
}