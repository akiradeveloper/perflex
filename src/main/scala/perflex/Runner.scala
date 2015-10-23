package perflex

import java.util.concurrent.{TimeUnit, Executors}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Try, Failure, Success}

class Runner[K](tasks: Seq[Unit => K]) {

  case class Result(result: Seq[Option[K]], execTime: Long)

  var concurrentN = 1

  def concurrentNumber(n: Int) = { concurrentN = n; this }

  def run = {
    implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(concurrentN))
    val start = System.currentTimeMillis
    val futs =
      Future.sequence(
        tasks.map { t => Future { t.apply() } }
        .map(_.map(Success(_)).recover { case e => Failure(e) })
      )
    val rets = Await.result(futs, Duration.Inf).map(_.toOption)
    val end = System.currentTimeMillis
    executionContext.shutdown
    Result(rets, end - start)
  }
}
