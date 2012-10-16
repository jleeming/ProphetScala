package code.lib.cache

import akka.actor._
import akka.routing.SmallestMailboxRouter
import akka.util.Duration
import akka.util.duration._
import scala.collection.mutable.HashMap
import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import code.model.Currency

case class GetInfo(key: String)
case class SetInfo(key: String, currencyList: Seq[Currency])
case object Clear

class CacheMaster extends Actor { 
  
  var inBuffer: HashMap[String, ListBuffer[ActorRef]] = HashMap()
  val maxBufferSize = 100
  var bufferSize = 0
  
  val cacheRouter = context.actorOf(Props[CacheManager].withRouter(SmallestMailboxRouter(20)), name = "cacheRouter")
  
  context.setReceiveTimeout(10 milliseconds)

  def receive = {
    case GetInfo(key) => {
      if (bufferSize > maxBufferSize) {
        sendByBuffer
        bufferSize = 0
      } else {
        bufferSize += 1
        if (inBuffer.contains(key)) {
          var list = inBuffer(key)
          list.append(sender)
          inBuffer += ((key, list))
        } else {
          inBuffer += ((key, ListBuffer(sender)))
        }
      }
    }  
    case SetInfo(key, currencyList) =>
      cacheRouter ! SetInfo(key, currencyList)
    case Clear =>
      cacheRouter ! Clear
    case ReceiveTimeout => sendByBuffer
  }
  
  def sendByBuffer = {
    if (inBuffer.size > 0) {
      inBuffer.keySet.foreach(key => {
        val actors = inBuffer(key).toList
        cacheRouter ! GetStruct(key, actors)
      })
      inBuffer.clear()
    }
  }
}