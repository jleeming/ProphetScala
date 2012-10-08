package code.lib
import code.model.Currency

object Ema {
  def calculate(rates: Seq[Currency]): BigDecimal = {
    calculateLoop(rates, 2.0 / (rates.size + 1))
  }

  private def calculate(x: Seq[Currency], k: BigDecimal): BigDecimal = {
    println(x.size)
    if (x.size == 1) x(0).value else k * x(x.size - 1).value + (1 - k) * calculate(x.slice(0, x.size - 1), k)
  }
  
  private def calculateLoop(x: Seq[Currency], k: BigDecimal): BigDecimal = {
    
    x.foldLeft[BigDecimal](x.head.value)((a, b) => {
      k * b.value + (1 - k) * a
    })    
    
  }
}