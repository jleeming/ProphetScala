package code.lib
import code.model.CurrencyForecast

object Prophet {
    def forecast(currencyType: String): CurrencyForecast = {
        val rates = RateReader.readRatesWithCache(currencyType)
        CurrencyForecast.create(currencyType, rates, Ema.calculate(rates))
    }
}