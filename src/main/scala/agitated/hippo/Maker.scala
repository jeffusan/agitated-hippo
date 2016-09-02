package agitated.hippo

import java.util.Date

import com.thinkaurelius.titan.core.TitanFactory
import gremlin.scala._

import scala.util.Random

case class Person(firstName: String, lastName: String) {

  def name = {
    s"$firstName $lastName"
  }
}

case class Town(name: String, nihongo: String, prefecture: String, density: java.lang.Long, population: Int, area: java.lang.Long, founded: Date)

object Maker {

  def main(args: Array[String]) = {
    val graph = TitanFactory.open(conf)

    val people = namesToPeople("names.txt").map(p => graph + ("person", FirstName -> p.firstName, LastName -> p.lastName))
    println(s"created ${people.size} people")

    val vehicles = fileToSeq("vehicles.txt").map(v => graph + v)
    println(s"created ${vehicles.size} vehicles")
    val towns = placesToTowns("places.csv").map { t =>
      graph + (
        t.name,
        Nihongo -> t.nihongo,
        Prefecture -> t.prefecture,
        Population -> t.population,
        Density -> t.density,
        Area -> t.area,
        Founded -> t.founded
        )
    }

    println(s"created ${towns.size} towns")
    graph.close()
  }

  def namesToPeople(fileName: String): Seq[Person] = fileToSeq(fileName).map(n => new Person(n.split(" ")(0), n.split(" ")(1)))

  def placesToTowns(fileName: String): Seq[Town] = {
    fileToSeq(fileName).map { p =>
      val ps = p.split(",").map(t => t.trim).toSeq
      Town(
        name = ps(0),
        nihongo = ps(1),
        prefecture = ps(2),
        population = ps(3).toInt,
        density = java.lang.Long.valueOf(ps(4).toLong),
        area = java.lang.Long.valueOf(ps(5).toLong),
        founded = format.parse(ps(6))
      )
    }
  }

  def getFriends(friends: Seq[Person]): Seq[Person] = {
    // given an .8 percent dist for a sample of 200, how many friends do we have?
    // my guess is about 100
    val friendCount = tf(0.8).sample(200).count(a => !a)
    Random.shuffle(friends).take(friendCount)
  }

}
