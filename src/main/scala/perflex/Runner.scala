package perflex

case class Runner() {
  def concurrentNumber(n: Int): this.type = this
  def run[K](tasks: Seq[Any => K]): Seq[Option[K]] = Seq()
}
