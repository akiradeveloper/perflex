package perflex.app

import perflex.{Runner}

import scala.util.Random

object Parallel extends App {
  val tasks =
    Stream.fill(1000) { (_:Any) =>
      val rand = Random.alphanumeric.take(5).mkString
      println("start - " + rand)
      Thread.sleep(100)
      println("end   - " + rand)
    }

  val result = new Runner(tasks)
    .concurrentNumber(100)
    .run
}
