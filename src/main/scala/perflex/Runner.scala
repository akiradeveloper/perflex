package perflex

import java.util.concurrent.{TimeUnit, Executors}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

class Runner[K](tasks: Seq[Unit => K]) {

  case class Result(result: Seq[Option[K]], execTime: Long)

  var concurrentN = 1

  def concurrentNumber(n: Int) = { concurrentN = n; this }

  def run = {
    implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(concurrentN))
    val start = System.currentTimeMillis
    val rets = tasks.map { t =>
      val fut = Future {
        t.apply()
      }
      Await.ready(fut, Duration.Inf)
      fut.value.get.toOption
    }.toList
    val end = System.currentTimeMillis
    executionContext.shutdown
    Result(rets, end - start)
  }
}
