package perflex

import perflex.reporter.Reporter

case class ReportMaker[R <: Reporter](reporters: Seq[R]) {
  def make[K](result: Seq[Option[K]]): String = reporters(0).report(result) // tmp
}
