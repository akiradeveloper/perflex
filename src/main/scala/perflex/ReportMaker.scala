package perflex

import perflex.reporter.Reporter
import scala.collection.mutable

class ReportMaker[K](result: Seq[Option[K]]) {
  val reporters = new mutable.ListBuffer[Reporter[K]]
  def withReporters(reporters: Seq[Reporter[K]]) = { this.reporters ++= reporters; this }
  def make: String = reporters.map { r =>
    r.report(result)
  }.mkString("\n")
}
