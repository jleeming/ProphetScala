package code.lib.cache

import akka.actor._
import akka.routing.RoundRobinRouter
import akka.util.Duration
import akka.util.duration._

class CacheMaster extends Actor { 
  
  val cacheRouter = context.actorOf(Props[CacheManager].withRouter(RoundRobinRouter(10)), name = "cacheRouter")
  
  context.setReceiveTimeout(30 milliseconds)

  def receive = {
    // накапливать просителей
    case ReceiveTimeout ⇒ throw new RuntimeException("received timeout") // кидать запрос
  }
}