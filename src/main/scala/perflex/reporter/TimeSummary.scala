package perflex.reporter

import perflex.statkind._

class TimeSummary[K <: Time] extends Reporter[K] {
  override def report(result: Seq[Option[K]]): String = {
    val times = result.filter(a => a.isDefined).map(_.get.time)
    val bui = new StringBuilder
    bui.append("Summary:\n")
    if (times.isEmpty) {
      return "  No valid response was made"
    }
    val fastest = times.min
    val slowest = times.max
    val total = times.sum
    val average = total.toFloat / times.length
    bui.append(  s"Total:        $total\n")
    bui.append(  s"Slowest:      $slowest\n")
    bui.append(  s"Fastest:      $fastest\n")
    bui.append(  s"Average:      $average\n")
    bui.append(  s"Requests/sec: ${1 / average}\n")
    bui.result
  }
}
