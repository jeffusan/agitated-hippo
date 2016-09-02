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

  "Schema cleaner" should "connect to a Titan database, and nuke everything" in {
    val graph = connect()
//    val graph = TitanFactory.open(conf)

    Schema.defineSchema(graph.openManagement())
    graph.close()

    // this is a development tool
    // it should clear both the data and any indices
    TitanCleanup.clear(graph)
  }
}
