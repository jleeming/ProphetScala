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

object RateReader {
  val dayCount = 7
  val formatter: SimpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy")
  val formatter2: SimpleDateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy")
  val currencyTypeCode: HashMap[String, String] = HashMap(("USD" -> "R01235"), ("EUR" -> "R01239"), ("GBP" -> "R01035"))

  def readRates(currencyType: String): Seq[Currency] = {
    import net.liftweb.util.TimeHelpers._
    val history = readRates(currencyType, (dayCount * 2).days.ago, now)
    if (history.size >= dayCount) history.slice(0, dayCount) else readRates(currencyType, 30.days.ago, now).slice(0, dayCount)
  }

  def readJson(currencyType: String): JValue = {
    JString(readRates(currencyType).foldLeft("")((a, b) => a + b.date + "->" + b.value + " "))
  }

  private def readRates(currencyType: String, beginDate: Date, endDate: Date): Seq[Currency] = {
    import java.net.{ URLConnection, URL }
    val url = new URL("""http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=""" + formatter.format(beginDate) + """&date_req2=""" + formatter.format(endDate) + """&VAL_NM_RQ=""" + currencyTypeCode.get(currencyType).get)
    val conn = url.openConnection
    val history: Elem = XML.load(conn.getInputStream)
    val result: Seq[Currency] = (history \ "Record").map { record => { Currency(formatter2.parse((record \ "@Date").text), BigDecimal((record \ "Value").text.replace(",", "."))) } }
    result.reverse
  }
}