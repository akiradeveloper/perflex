package perflex

import java.util.concurrent.Executors

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

class Runner[K](tasks: Seq[Any => K]) {
  def concurrentNumber(n: Int): this.type = this

  def run: Seq[Option[K]] = {
    implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
    tasks.map { t =>
      val fut = Future {
        t.apply(0)
      }
      Await.ready(fut, Duration.Inf)
      fut.value.get.toOption
    }
  }
}
