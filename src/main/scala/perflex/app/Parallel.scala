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

  val start = System.currentTimeMillis
  val result = new Runner(tasks)
    .concurrentNumber(100)
    .run
  val end = System.currentTimeMillis

  // N
  // 10 -> 10000 msec
  // 100 -> 1200 msec
  // 1000 -> 400 msec
  println(s"${end - start} msec")
}
