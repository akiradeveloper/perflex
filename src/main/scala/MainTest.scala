import perflex.{ReportMaker, Runner, _}

object MainTest extends App {

  case class MyType(time: Long, statusCode: Int) extends Object
  with statkind.Time
  with statkind.StatusCode

  val tasks = Stream(
    (_: Any) => MyType(111, 200),
    (_: Any) => MyType(222, 403),
    (_: Any) => { assert(false); MyType(333, 200) }
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
