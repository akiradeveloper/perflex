package perflex.reporter

import perflex.statkind._

case class TimeSummary() extends Reporter {
  override def report[K <: Time](result: Seq[Option[K]]): String = "summary"
}
