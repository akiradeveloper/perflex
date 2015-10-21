package perflex.reporter

import perflex.Runner

trait Reporter[K] {
  def report(result: Runner[K]#Result): String
}
