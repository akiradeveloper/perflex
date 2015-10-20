package perflex.reporter

import perflex.statkind._

class TimeSummary[K <: Time] extends Reporter[K] {
  override def report(result: Seq[Option[K]]): String = "summary"
}
