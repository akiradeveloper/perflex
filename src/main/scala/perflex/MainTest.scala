package perflex

import perflex.reporter._

object MainTest extends App {

  case class MyType(time: Long) extends statkind.Time

  val tasks: Seq[Any => MyType] = Seq(
    (a: Any) => MyType(777)
  )

  val runner = Runner().concurrentNumber(8)
  val result: Seq[Option[MyType]] = runner.run(tasks)

  val reporters = Seq(TimeSummary(), TimeDistribution())
  val reporter = ReportMaker(reporters)

  println(reporter.make(result))
}
