package perflex.reporter

import perflex.statkind.Time

class TimeDistribution[K <: Time] extends Reporter[K] {
  override def report(result: Seq[Option[K]]): String = "dist"
}
