package agitated.hippo
import gremlin.scala._
import com.thinkaurelius.titan.core.TitanFactory


object Finder {

  def main(args: Array[String]) = {

    val graph = TitanFactory.open(conf)

    println(s"Graph Size: ${graph.V.toList().size}")

    println(s"Indexes:")

    println(graph.openManagement().getVertexLabels)

    graph.close()
  }
}
