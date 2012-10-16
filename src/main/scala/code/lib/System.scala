package code.lib

import akka.actor.ActorSystem
import code.lib.cache.CacheMaster

object System {
  val system = ActorSystem("actorSystem")
  val cacheMaster = system.actorOf(akka.actor.Props[CacheMaster], name = "cacheMaster")
}