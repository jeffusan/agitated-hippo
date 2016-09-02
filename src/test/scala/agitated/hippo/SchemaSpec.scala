package agitated.hippo

import com.thinkaurelius.titan.core.TitanGraph
import org.scalatest._
import com.thinkaurelius.titan.core.TitanFactory
import com.thinkaurelius.titan.core.util.TitanCleanup

trait InMemoryConnect {
  def connect(): TitanGraph = {
    import org.apache.commons.configuration.BaseConfiguration
    val conf = new BaseConfiguration()
    conf.setProperty("storage.backend","inmemory")
    TitanFactory.open(conf)
  }
}

class SchemaSpec extends FlatSpec with Matchers with InMemoryConnect {

  "Gremlin-scala" should "connect to a Titan database, pull out Saturn's key and shutdown cleanly" in {
    val graph = connect()

    Schema.defineSchema(graph.openManagement())
    graph.close()

    TitanCleanup.clear(graph)
  }
}
