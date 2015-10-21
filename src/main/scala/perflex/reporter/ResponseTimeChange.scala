package perflex.reporter

import perflex.Runner
import perflex.statkind.Time
import scala.collection.mutable

class ResponseTimeChange[K <: Time] extends Reporter[K] {
  override def report(result: Runner[K]#Result): String = {
    val bui = new StringBuilder
    bui.append("Response time change:\n")
    if (result.result.filter(_.isDefined).isEmpty) {
      bui.append("  No valid reponse was made\n")
      return bui.result
    }

    val WIDTH = 20
    val count = mutable.ArrayBuffer.fill(WIDTH)(0)
    val total = mutable.ArrayBuffer.fill(WIDTH)(0f)

    result.result.zipWithIndex.foreach {
      case (Some(a), i) => {
        val coord = (WIDTH.toFloat * i / result.result.length).toInt
        count(coord) += 1
        total(coord) += a.time
      }
      case _ => {}
    }

    val descaler = result.result.filter(_.isDefined).map(_.get.time).max / 50
    val bar: Seq[Option[Int]] = total.zip(count).map {
      case (_, 0) => None
      case (a, n) => Some((a / n / descaler).toInt)
    }
    bar.map {
      case Some(a) => {
        Seq.fill(a-1)(" ").mkString + "*"
      }
      case None => { "" }
    }.foreach { a => bui.append("  |" + a + "\n") }

    bui.result
  }
}
