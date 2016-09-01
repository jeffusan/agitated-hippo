import org.scalatest.{FlatSpec, Matchers}
import probability_monad.Distribution
import probability_monad.Distribution.{Coin, H}


class BiasedCoinSpec  extends FlatSpec with Matchers {

  "BiasedCoin" should "flip a bunch of times and be either heads or tails" in {

    val uniform = new Distribution[Double] {
      private val rand = new java.util.Random()
      override def get = rand.nextDouble()
    }

    def tf(p: Double): Distribution[Boolean] = {
      uniform.map(_ < p)
    }

    val results = tf(0.8).sample(100).count(a => !a)
    println(results)
  }
}