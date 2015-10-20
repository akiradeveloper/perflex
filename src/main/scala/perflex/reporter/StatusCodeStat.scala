package perflex.reporter

import perflex.statkind._
import scala.collection.mutable

class StatusCodeStat[K <: StatusCode] extends Reporter[K] {
  override def report(result: Seq[Option[K]]): String = {
    val m = new mutable.HashMap[Int, Int]
    result.foreach {
      case Some(a) => m(a.statusCode) = m.getOrElse(a.statusCode, 0) + 1
      case _ => {}
    }
    val bui = new StringBuilder
    bui.append("StatusCode stat:\n")
    m.toList.sortBy(_._1).foreach { case (k, v) =>
      bui.append(s"  ${k}[${v}]\n")
    }
    bui.result
  }
}
