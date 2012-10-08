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

import code.lib.cache.CacheManager

/**
 * A full REST example
 */
object FullRest extends RestHelper {
    
  // REST path
  serve {

    case "rest" :: "test" :: Nil JsonGet _ => JString(net.liftweb.util.Props.get("memcachedURL", "error"))

    case "rest" :: "close" :: Nil JsonGet _ => {
      CacheManager.shutdown
      JString("OK")
    }
    
    case "cache" :: Nil JsonDelete _ => {
      CacheManager.clear
      JString("OK")
    }
    
    case "currency" :: currencyType :: Nil JsonGet _ => Prophet.forecast(currencyType.toUpperCase()): JValue

  }
}
