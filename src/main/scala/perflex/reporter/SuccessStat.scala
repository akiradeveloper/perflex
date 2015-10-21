package perflex.reporter

import perflex.Runner

class SuccessStat[K] extends Reporter[K] {
  override def report(result: Runner[K]#Result) = {
    var success = 0
    var failure = 0
    result.result.foreach {
      case Some(_) => success += 1
      case None => failure += 1
    }
    val bui = new StringBuilder
    bui.append("Success/Failure:\n")
    bui.append(s"  Success: $success\n")
    bui.append(s"  Failure: $failure\n")
    bui.result
  }
}
