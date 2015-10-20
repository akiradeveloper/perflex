package perflex.reporter

import perflex.statkind.Time

case class Range(min: Float, max: Float) {
  var count: Int = 0
  def median: Float = (min + max) / 2
  def take(v: Float): Boolean = {
    if (min <= v && v <= max) {
      count += 1
      true
    } else {
      false
    }
  }
}

class TimeDistribution[K <: Time] extends Reporter[K] {
  override def report(result: Seq[Option[K]]): String = {
    val times: Seq[Float] = result.filter(_.isDefined).map(_.get.time.toFloat)
    val bui = new StringBuilder
    bui.append("Response time histogram:\n")
    if (times.isEmpty) {
      bui.append("  No valid response was made\n")
      return bui.result
    }
    val N = Math.min(times.length, 10)
    val min = times.min
    val max = times.max
    val interval = (max - min) / N
    val ranges: Seq[Range] = (0 to N-1).toSeq.map(_ * interval + min).map(a => Range(a, a + interval))
    times.foreach { time =>
      ranges.foreach { range =>
        range.take(time)
      }
    }
    val maxCount = ranges.map(_.count).max
    val descaler = if (maxCount > 20) {
      maxCount / 20.0
    } else {
      1.0
    }
    ranges.foreach { range =>
      bui.append(s"  ${"%11f".format(range.median)} ${"[%10d]".format(range.count)} | ${List.fill((range.count / descaler).toInt)("*").mkString}\n")
    }
    bui.result
  }
}
