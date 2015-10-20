package perflex.reporter

import perflex.statkind.Time

trait Reporter {
  def report[K](result: Seq[Option[K]]): String
}
