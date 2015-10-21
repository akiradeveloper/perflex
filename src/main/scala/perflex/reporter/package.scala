package perflex

import perflex.statkind.{StatusCode, Time}

package object reporter {
  def TimeAll[T <: Time] = Seq(
    new SuccessStat[T],
    new TimeSummary[T],
    new TimeDistribution[T],
    new ResponseTimeChange[T]
  )

  def StatusCodeAll[T <: StatusCode] = Seq(
    new StatusCodeStat[T]
  )
}
