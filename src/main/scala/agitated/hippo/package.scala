package agitated

import java.io.File

import gremlin.scala.Key

import scala.io.Source
import org.apache.commons.configuration.BaseConfiguration
import probability_monad.Distribution


package object hippo {

  val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

  val conf = new BaseConfiguration()
  conf.setProperty("storage.backend","cassandrathrift")
  conf.setProperty("storage.hostname", "127.0.0.1")
  conf.setProperty("storage.batch-loading", "true")

  val base_dir = "src/main/resources"

  val Person = Key[String]("person")
  val FirstName = Key[String]("firstName")
  val LastName = Key[String]("lastName")

  val Vehicle = Key[String]("vehicle")

  val Nihongo = Key[String]("nihongo")
  val Area = Key[Double]("area")
  val Prefecture = Key[String]("prefecture")

  def fileToSeq(name: String) = {
    val f = new File(s"$base_dir/$name")
    Source.fromFile(f).getLines.toSeq
  }

  def stripAndConc(firstName: String, lastName: String) = {
    s"${firstName.trim} ${lastName.trim}"
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

}
