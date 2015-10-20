package perflex.reporter

import perflex.statkind._

class StatusCodeStat[K <: StatusCode] extends Reporter[K] {
  override def report(result: Seq[Option[K]]): String = "statusCode"
}
