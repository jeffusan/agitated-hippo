package agitated.hippo

import java.util.Date

import com.thinkaurelius.titan.core.TitanFactory
import com.thinkaurelius.titan.core.schema.TitanManagement
import org.apache.tinkerpop.gremlin.process.traversal.Order
import org.apache.tinkerpop.gremlin.structure.{Direction, Vertex}


object Schema {

  def main(args: Array[String]) = {
    val graph = TitanFactory.open(conf)
    defineSchema(graph.openManagement())
    graph.close()
  }

  def defineSchema(m: TitanManagement) = {

    // vertex labels
    val person = m.makeVertexLabel("person").make()

    val vehicle   = m.makeVertexLabel("vehicle").make()

    val town = m.makeVertexLabel("town").make()

    // edge labels

    val friendOf    = m.makeEdgeLabel("friendOf").make()

    val vehicleOf  = m.makeEdgeLabel("vehicleOf").make()

    val livesIn = m.makeEdgeLabel("livesIn").make()


    // vertex and edge properties
    val blid = m.makePropertyKey("bulkLoader.vertex.id").dataType(classOf[String]).make()
    val name = m.makePropertyKey("name").dataType(classOf[String]).make()
    val firstName = m.makePropertyKey("firstName").dataType(classOf[String]).make()
    val lastName = m.makePropertyKey("lastName").dataType(classOf[String]).make()
    val japaneseName     = m.makePropertyKey("japaneseName").dataType(classOf[String]).make()
    val prefecture = m.makePropertyKey("prefecture").dataType(classOf[String]).make()
    val population = m.makePropertyKey("population").dataType(classOf[Integer]).make()
    val area = m.makePropertyKey("area").dataType(classOf[java.lang.Long]).make()
    val density = m.makePropertyKey("density").dataType(classOf[Integer]).make()
    val founded = m.makePropertyKey("founded").dataType(classOf[Date]).make()
    val age = m.makePropertyKey("age").dataType(classOf[Integer]).make()

    // global indices
    m.buildIndex("byBulkLoaderVertexId", classOf[Vertex]).addKey(blid).buildCompositeIndex()
    m.buildIndex("peopleByName", classOf[Vertex]).addKey(name).indexOnly(person).buildCompositeIndex()
    m.buildIndex("byFirstName", classOf[Vertex]).addKey(firstName).indexOnly(person).buildCompositeIndex()
    m.buildIndex("byLastName", classOf[Vertex]).addKey(lastName).indexOnly(person).buildCompositeIndex()

    // vertex centric indices
    m.buildEdgeIndex(friendOf, "friendOfName", Direction.BOTH, Order.decr, name)
    m.commit()
  }

}
