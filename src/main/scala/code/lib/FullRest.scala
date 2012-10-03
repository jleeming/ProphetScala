package code
package lib

import model._

import net.liftweb._
import common._
import http._
import rest._
import util._
import Helpers._
import json._
import scala.xml._

/**
 * A full REST example
 */
object FullRest extends RestHelper {

  // REST path
  serve {

    case "rest" :: "read" :: currencyType :: Nil JsonGet _ => RateReader.readJson(currencyType)

    case "currency" :: currencyType :: Nil JsonGet _ => Prophet.forecast(currencyType): JValue

  }
}
