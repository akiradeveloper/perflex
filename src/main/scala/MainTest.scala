import perflex.{ReportMaker, Runner, _}

import scala.util.Random

object MainTest extends App {

  case class MyType(time: Float, statusCode: Int) extends Object
  with statkind.Time
  with statkind.StatusCode

  val tasks = Stream.fill(1000)(Random.nextInt % 1000).map(a => (_:Any) => MyType(a.abs, 200)) ++ Stream(
    (_: Any) => MyType(111, 200),
    (_: Any) => MyType(222, 403),
    (_: Any) => MyType(2, 403),
    (_: Any) => MyType(3, 403),
    (_: Any) => { assert(false); MyType(333, 200) }
  )

  val result = new Runner(tasks)
    .concurrentNumber(8)
    .run

  val report = new ReportMaker(result).make(
    Seq(
      new reporter.SuccessStat,
      new reporter.TimeSummary,
      new reporter.TimeDistribution,
      new reporter.ResponseTimeChange,
      new reporter.StatusCodeStat
    )
  )

  println(report)
}
