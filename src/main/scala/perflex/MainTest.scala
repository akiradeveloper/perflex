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

  val reporters = Seq(
    new TimeSummary[MyType],
    new TimeDistribution[MyType],
    new StatusCodeStat[MyType]
  )
  val reporter = new ReportMaker[MyType](reporters)

  println(reporter.make(result))
}
