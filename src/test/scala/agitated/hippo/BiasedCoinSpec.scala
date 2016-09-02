package agitated.hippo

import org.scalatest.{FlatSpec, Matchers}


class BiasedCoinSpec  extends FlatSpec with Matchers {

  "BiasedCoin" should "flip a bunch of times and be either heads or tails" in {

    (1 to 20).foreach { i =>
      val results = tf(0.8).sample(100).count(a => !a)
      println(results)
    }

  }
}