package code.lib

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST.JString
import scala.io.Source
import scala.io.Codec
import scala.io.BufferedSource
import scala.collection.immutable.HashMap
import scala.xml._
import java.util.Date
import java.text.SimpleDateFormat
import code.model.Currency

import net.liftweb.util.TimeHelpers._
import code.lib.cache.CacheManager

object RateReader {
  val DAY_COUNT = 7
  val formatter: SimpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy")
  val formatter2: SimpleDateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy")
  val currencyTypeCode: HashMap[String, String] = HashMap(("USD" -> "R01235"), ("EUR" -> "R01239"), ("GBP" -> "R01035"))

  def readRatesWithCache(currencyType: String): Seq[Currency] = {
    val fromCache = CacheManager.get(generateKey)
    if (fromCache == null) {
      val rates = readRates(currencyType)
      CacheManager.set(generateKey, rates)
      rates
    } else {
      fromCache
    } 
  }
  
  private def generateKey: String = {
    formatter2.format(now) + ":" + DAY_COUNT
  }
  
  def readRates(currencyType: String): Seq[Currency] = {    
    val history = readRates(currencyType, (DAY_COUNT * 2).days.ago, now)
    if (history.size >= DAY_COUNT) history.slice(history.size - DAY_COUNT, history.size) else readRates(currencyType, 30.days.ago, now).slice(history.size - DAY_COUNT, history.size -1)
  }

  private def readRates(currencyType: String, beginDate: Date, endDate: Date): Seq[Currency] = {
    val history = XML.load("""http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=""" + formatter.format(beginDate) + """&date_req2=""" + formatter.format(endDate) + """&VAL_NM_RQ=""" + currencyTypeCode.get(currencyType).get)
    val result: Seq[Currency] = (history \ "Record").map { record => { Currency(formatter2.parse((record \ "@Date").text), BigDecimal((record \ "Value").text.replace(",", "."))) } }
    result
  }
}