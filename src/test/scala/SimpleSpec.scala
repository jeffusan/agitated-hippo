import com.thinkaurelius.titan.core.TitanGraph
import org.apache.tinkerpop.gremlin.structure.T
import gremlin.scala._
import org.scalatest._

trait InMemoryConnect {
  def connect(): TitanGraph = {
    import org.apache.commons.configuration.BaseConfiguration
    val conf = new BaseConfiguration()
    conf.setProperty("storage.backend","cassandrathrift")
    conf.setProperty("storage.hostname", "127.0.0.1")
    import com.thinkaurelius.titan.core.TitanFactory
    TitanFactory.open(conf)
  }
}

class SimpleSpec extends FlatSpec with Matchers with InMemoryConnect {

  "Gremlin-scala" should "connect to a Titan database, pull out Saturn's key and shutdown cleanly" in {
    val graph = connect().asScala
    val name = Key[String]("name")
    val planet = "planet"
    val saturn = "saturn"
//
//    (1 to 5) foreach { i =>
//      graph + (planet, name -> s"vertex $i")
//    }
//
//    graph + (saturn, name -> saturn)
//
//    graph.V.count.head shouldBe 6
//
//    val traversal = graph.V.value(name)
//    traversal.toList.size shouldBe 6
//
//    graph.V.hasLabel(saturn).count.head shouldBe 1
//
//    val saturnQ = graph.V.hasLabel(saturn).head
//    saturnQ.value2(name) shouldBe saturn

    graph.close
  }
}
