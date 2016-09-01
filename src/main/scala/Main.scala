import java.io.File

import scala.io.Source

object Main {

  val base_dir = "src/main/resources"

  def main(args: Array[String]) = {
    val firsts = new File(s"$base_dir/first_names.txt")
    val lasts = new File(s"$base_dir/last_names.txt")
    val firstNames = Source.fromFile(firsts).getLines.toSeq
    val lastNames = Source.fromFile(lasts).getLines.toSeq

    val mixed = firstNames.flatMap(_a => lastNames.map(_b => stripAndConc(_a, _b))).seq
    println(mixed.length)
  }

  def stripAndConc(firstName: String, lastName: String) = {
    s"${firstName.trim} ${lastName.trim}"
  }
}
