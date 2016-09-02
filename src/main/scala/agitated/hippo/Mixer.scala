package agitated.hippo

import java.io.{File, PrintWriter}

import scala.io.Source

object Mixer {


  def main(args: Array[String]) = {

    val firstNames = fileToSeq("first_names.txt")
    val lastNames = fileToSeq("last_names.txt")
    val writer = new PrintWriter(new File(s"$base_dir/names.txt"))

    val mixed = firstNames.flatMap(_a => lastNames.map(_b => stripAndConc(_a, _b))).seq

    mixed foreach { name =>
      writer.write(s"$name\n")
    }
    writer.close()
  }

}
