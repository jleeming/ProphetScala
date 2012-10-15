package code.lib.prophet

import code.model.CurrencyForecast
import scala.actors.Actor
import code.lib._

//extends Actor
object Prophet {
  
//  def act() {
//    loop {
//      react {
//        case (currencyType: String, actor: Actor) =>
//          actor ! forecast(currencyType: String)
//        case "stop" =>  
//          exit()  
//      }
//    }
//  } 
  
  def forecast(currencyType: String): CurrencyForecast = {
    val rates = (new RateReader).readRatesWithCache(currencyType)
    new CurrencyForecast(currencyType, rates, Ema.calculate(rates))
  }
}