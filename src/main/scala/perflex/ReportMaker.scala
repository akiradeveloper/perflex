package perflex

import perflex.reporter.Reporter

class ReportMaker[K](result: Seq[Option[K]]) {
  def make(reporters: Seq[Reporter[K]]): String = reporters.map { r =>
    r.report(result)
  }.mkString("\n")
}
