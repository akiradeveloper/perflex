package perflex

import perflex.reporter.Reporter

class ReportMaker[K](reporters: Seq[Reporter[K]]) {
  def make(result: Seq[Option[K]]): String = reporters.map { r =>
    r.report(result)
  }.mkString("\n")
}
