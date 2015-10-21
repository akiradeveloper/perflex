package perflex

import java.util.concurrent.{TimeUnit, Executors}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

class Runner[K](tasks: Seq[Unit => K]) {

  var concurrentN = 1

  def concurrentNumber(n: Int) = { concurrentN = n; this }

  def run: Seq[Option[K]] = {
    implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(concurrentN))
    val ret = tasks.map { t =>
      val fut = Future {
        t.apply()
      }
      Await.ready(fut, Duration.Inf)
      fut.value.get.toOption
    }.toList
    executionContext.shutdown
    ret
  }
}
