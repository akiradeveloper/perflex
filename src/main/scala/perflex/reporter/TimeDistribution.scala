package perflex.reporter

import perflex.statkind.Time

case class TimeDistribution() extends Reporter {
  override def report[K <: Time](result: Seq[Option[K]]): String = "dist"
}
