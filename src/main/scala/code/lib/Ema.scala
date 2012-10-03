package code.lib
import code.model.Currency

object Ema {
  def calculate(rates: Seq[Currency]): BigDecimal = {
    calculate(rates, 2.0 / (rates.size + 1))
  }

  private def calculate(x: Seq[Currency], k: BigDecimal): BigDecimal = {
    if (x.size == 1) x(0).value else k * x.head.value + (1 - k) * calculate(x.tail, k)
  }
}