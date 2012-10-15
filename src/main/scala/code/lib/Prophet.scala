package code.lib
import code.model.CurrencyForecast

object Prophet {
    def forecast(currencyType: String): CurrencyForecast = {
        val rates = (new RateReader).readRatesWithCache(currencyType)
        new CurrencyForecast(currencyType, rates, Ema.calculate(rates))
    }
}