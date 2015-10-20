import perflex.{ReportMaker, Runner}
import perflex._

object MainTest extends App {

  case class MyType(time: Long, statusCode: Int) extends Object
  with statkind.Time
  with statkind.StatusCode

  val tasks = Stream(
    (a: Any) => MyType(777, 200)
  )

  val runner = new Runner(tasks).concurrentNumber(8)
  val result = runner.run

  val report = new ReportMaker(result).make(
    Seq(
      new reporter.TimeSummary,
      new reporter.TimeDistribution,
      new reporter.StatusCodeStat
    )
  )

  println(report)
}
