package perflex

import perflex.reporter.Reporter
import scala.collection.mutable

class ReportMaker[K](result: Option[Runner[K]#Result]) {
  val reporters = new mutable.ListBuffer[Reporter[K]]
  def withReporters(reporters: Seq[Reporter[K]]) = { this.reporters ++= reporters; this }
  def make: String = result match {
    case Some(a) => reporters.map(_.report(a)).mkString("\n")
    case None => "Some tests failed"
  }
}
