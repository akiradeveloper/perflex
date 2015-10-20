package perflex

class Runner[K](tasks: Seq[_ => K]) {
  def concurrentNumber(n: Int): this.type = this

  def run: Seq[Option[K]] = Seq() // tmp
}
