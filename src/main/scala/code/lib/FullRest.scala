package code
package lib

import net.liftweb._
import common._
import http._
import rest._
import util._
import Helpers._
import json._
import scala.xml._
import scala.actors.Actor._

/**
 * A full REST example
 */
object FullRest extends RestHelper {
    
  // REST path
  serve {

    case "rest" :: "test" :: Nil JsonGet _ => JString(net.liftweb.util.Props.get("memcachedURL", "error"))

    case "rest" :: "close" :: Nil JsonGet _ => {
      JString("OK")
    }
    
    case "cache" :: Nil JsonDelete _ => {
      JString("OK")
    }
    
    case "currency" :: currencyType :: Nil JsonGet _ => {
      JString("test " + currencyType)
    }

  }
}
