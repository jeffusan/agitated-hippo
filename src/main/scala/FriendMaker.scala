import java.io.File
import java.text.DateFormat
import java.util.Date

import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import probability_monad.Distribution

import scala.io.Source
import scala.util.Random
import gremlin.scala._

case class Person(firstName: String, lastName: String) {

  def name = {
    s"$firstName $lastName"
  }
}

case class Town(name: String, nihongo: String, prefecture: String, density: Double, population: Int, area: Double, founded: Date)

object FriendMaker {

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
  import org.apache.commons.configuration.BaseConfiguration
  val conf = new BaseConfiguration()
  conf.setProperty("storage.backend","cassandrathrift")
  conf.setProperty("storage.hostname", "127.0.0.1")
  import com.thinkaurelius.titan.core.TitanFactory
  val graph = TitanFactory.open(conf)

  val FirstName = Key[String]("firstName")
  val LastName = Key[String]("lastName")

  val Vehicle = Key[String]("vehicle")

  val Nihongo = Key[String]("nihongo")
  val Area = Key[Double]("area")
  val Prefecture = Key[String]("prefecture")

  def main(args: Array[String]) = {
    val people = namesToPeople("names.txt").map(p => graph + (p.name, FirstName -> p.firstName, LastName -> p.lastName))
    println(s"created ${people.size} people")
    val vehicles = fileToSeq("vehicles.txt").map(v => graph + v)
    println(s"created ${vehicles.size} vehicles")
    val towns = placesToTowns("places.csv").map { t =>
      graph + (
        t.name,
        Nihongo -> t.nihongo,
        Area -> t.area,
        Prefecture -> t.prefecture)}

    println(s"created ${towns.size} towns")
  }

  def namesToPeople(fileName: String): Seq[Person] = fileToSeq(fileName).map(n => Person(n.split(" ")(0), n.split(" ")(1)))

  def placesToTowns(fileName: String): Seq[Town] = {
    fileToSeq(fileName).map { p =>
      val ps = p.split(",").map(t => t.trim).toSeq
      Town(
        name = ps(0),
        nihongo = ps(1),
        prefecture = ps(2),
        population = ps(3).toInt,
        density = ps(4).toDouble,
        area = ps(5).toDouble,
        founded = format.parse(ps(6))
      )
    }
  }

  def fileToSeq(name: String): Seq[String] = {
    val f = new File(s"${NameMixer.base_dir}/$name")
    Source.fromFile(f).getLines.toSeq
  }

  // should give a uniform probability
  val uniform = new Distribution[Double] {
    private val rand = new java.util.Random()
    override def get = rand.nextDouble()
  }

  // returns a distribution of true/falses
  def tf(p: Double): Distribution[Boolean] = {
    uniform.map(_ < p)
  }

  def getFriends(friends: Seq[Person]): Seq[Person] = {
    // given an .8 percent dist for a sample of 200, how many friends do we have?
    // my guess is about 100
    val friendCount = tf(0.8).sample(200).count(a => !a)
    Random.shuffle(friends).take(friendCount)
  }

}
