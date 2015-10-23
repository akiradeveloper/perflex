package perflex.reporter

import perflex.Runner
import perflex.statkind._

class TimeSummary[K <: Time] extends Reporter[K] {
  override def report(result: Runner[K]#Result): String = {
    val times = result.result.map(_.time)
    val bui = new StringBuilder
    bui.append("Summary:\n")

    val fastest = times.min
    val slowest = times.max
    val total = times.sum
    val average = total / times.length
    bui.append(s"  Total:   ${result.execTime} msec\n")
    bui.append(s"  Slowest: $slowest\n")
    bui.append(s"  Fastest: $fastest\n")
    bui.append(s"  Average: $average\n")
    // bui.append(  s"Requests/sec: ${1 / average}\n")
    bui.result
  }
}
