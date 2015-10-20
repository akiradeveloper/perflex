package perflex.reporter

trait Reporter[K] {
  def report(result: Seq[Option[K]]): String
}
