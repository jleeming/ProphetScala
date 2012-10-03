package code.model

import net.liftweb._
import util._
import Helpers._
import common._
import json._
import scala.xml.Node
import code.lib.BigDecimalSerializer
import java.util.Date
import net.liftweb.util.TimeHelpers
import code.lib.DateTimeSerializer
import java.util.UUID

case class CurrencyForecast(date: Date, currency: String, guid: String, history: Seq[Currency], future: Currency)

object CurrencyForecast {
    private implicit val formats =
        net.liftweb.json.DefaultFormats + BigDecimalSerializer + DateTimeSerializer

    def create(currencyType: String, rates: Seq[Currency], forecast: BigDecimal): CurrencyForecast = CurrencyForecast(1.days.later, currencyType, UUID.randomUUID().toString(), rates, Currency(1.days.later, forecast))

    /**
     * Convert the item to JSON format.  This is
     * implicit and in the companion object, so
     * an CurrencyForecast can be returned easily from a JSON call
     */
    implicit def toJson(cf: CurrencyForecast): JValue =
        Extraction.decompose(cf)
}