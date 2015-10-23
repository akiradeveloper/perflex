import java.util.concurrent.TimeUnit

import com.google.common.base.Stopwatch
import perflex.{ReportMaker, Runner, _}

import scala.util.Random

object Fake extends App {

  case class MyType(time: Float, statusCode: Int) extends Object
  with statkind.Time
  with statkind.StatusCode

  val tasks =
    Stream.fill(1000)(Random.nextInt % 1000).map(a => () => MyType(a.abs, 200)) ++
    Stream(
      () => MyType(111, 201),
      () => MyType(222, 403),
      () => MyType(2, 403),
      () => MyType(3, 403)
    )

  val result = new Runner(tasks)
    .concurrentNumber(16)
    .run

  val report = new ReportMaker(result)
    .withReporters(Seq(
      new reporter.TimeSummary,
      new reporter.TimeDistribution,
      new reporter.ResponseTimeChange
    ))
    .withReporters(reporter.StatusCodeAll)
    .make

  println(report)
}
