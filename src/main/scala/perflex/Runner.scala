package perflex

import java.util.concurrent.{TimeUnit, Executors}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Try, Failure, Success}

class Runner[K](tasks: Seq[Unit => K]) {

  case class Result(result: Seq[K], execTime: Long)

  var concurrentN = 1

  def concurrentNumber(n: Int) = { concurrentN = n; this }

  def run: Option[Result] = {
    implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(concurrentN))
    val start = System.currentTimeMillis
    val futs = Future.sequence(tasks.map { t => Future { t.apply() } } )
    Await.ready(futs, Duration.Inf)
    val rets = futs.value.get.toOption
    val end = System.currentTimeMillis
    executionContext.shutdown
    rets match {
      case Some(a) => Some(Result(a, end - start))
      case None => None
    }
  }
}
