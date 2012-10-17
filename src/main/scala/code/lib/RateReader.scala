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
import code.lib.cache.GetInfo
import code.lib.cache.SetInfo
import akka.actor._
import akka.dispatch.Await
import akka.pattern.ask
import code.lib.cache.CacheMaster

class RateReader {
  val DAY_COUNT = 7
  val formatter: SimpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy")
  val formatter2: SimpleDateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy")
  val currencyTypeCode: HashMap[String, String] = HashMap(("USD" -> "R01235"), ("EUR" -> "R01239"), ("GBP" -> "R01035"))

  def readRatesWithCache(currencyType: String): Seq[Currency] = {
    val cacheMaster = System.cacheMaster
    
    var result: Seq[Currency] = null
    var i: Int = 0
    
    while ((i < 2) && (result == null)) { 
      try {
        implicit val timeout = akka.util.Timeout(500)
        val future = cacheMaster ? GetInfo(generateKey(currencyType)) // enabled by the “ask” import
        result = Await.result(future, timeout.duration).asInstanceOf[Seq[Currency]]
      } catch {
        case e: Exception =>
      }
      i += 1
    }  
    
    if (result == null) {
      println(" --- CBR call ---")
      val rates = readRates(currencyType)
      cacheMaster ! SetInfo(generateKey(currencyType), rates)
      rates
    } else {
      result
    }
  }
  
  private def generateKey(currencyType: String): String = {
    currencyType + ":" + formatter2.format(now) + ":" + DAY_COUNT
  }
  
  def readRates(currencyType: String): Seq[Currency] = {
    val history = readRates(currencyType, (DAY_COUNT * 2).days.ago, now)
    if (history.size >= DAY_COUNT) history.slice(history.size - DAY_COUNT, history.size) else readRates(currencyType, 30.days.ago, now).slice(history.size - DAY_COUNT, history.size)
  }

  private def readRates(currencyType: String, beginDate: Date, endDate: Date): Seq[Currency] = {
    val history = XML.load("""http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=""" + formatter.format(beginDate) + """&date_req2=""" + formatter.format(endDate) + """&VAL_NM_RQ=""" + currencyTypeCode.get(currencyType).get)
    val result: Seq[Currency] = (history \ "Record").map { record => { Currency(formatter2.parse((record \ "@Date").text), BigDecimal((record \ "Value").text.replace(",", "."))) } }
    result
  }
}