package perflex

import perflex.reporter._

object MainTest extends App {

  case class MyType(time: Long, statusCode: Int) extends Object
  with statkind.Time
  with statkind.StatusCode

  val tasks: Seq[Any => MyType] = Seq(
    (a: Any) => MyType(777, 200)
  )

  val runner = new Runner().concurrentNumber(8)
  val result: Seq[Option[MyType]] = runner.run(tasks)

  val reporter = new ReportMaker[MyType](Seq(
    new TimeSummary,
    new TimeDistribution,
    new StatusCodeStat
  ))

  println(reporter.make(result))
}
