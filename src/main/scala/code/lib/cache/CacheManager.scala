package code.lib.cache

import code.model.Currency
import net.spy.memcached.MemcachedClient
import java.net.InetSocketAddress

import akka.actor._

case class GetStruct(key: String, actors: List[ActorRef])


class CacheManager extends Actor {
  
  private val EXPIRE: Int = 3600 * 24 * 7;
//  private val WAIT: Long = 5000;
  
  var client: MemcachedClient = null
  
  override def preStart = {
    init
  }
  
  def receive = {
        case GetStruct(key, actors) =>
          val result = get(key)
          actors.foreach(actor => {actor ! result})
        case SetInfo(key, currencyList) =>
          set(key, currencyList)
        case Clear =>
          clear
  }
  
  override def postStop = {
    shutdown
  }
  
  def init = {
    val server: String = net.liftweb.util.Props.get("memcachedURL", "localhost")
    val port: Int = net.liftweb.util.Props.getInt("memcachedPort", 11211) 
    client = new MemcachedClient(new InetSocketAddress(server, port));
    println(this.hashCode() + " CacheManager start")
  }
  
  def shutdown = {
    client.shutdown()
    println(this.hashCode() + " CacheManager stop")
  }
  
  def set(key: String, currencyList: Seq[Currency]) = {
//    if (!isClose) 
    client.set(key, EXPIRE, currencyList)
  }

  def get(key: String): Seq[Currency] = {
//    if (isClose) null
    client.get(key).asInstanceOf[Seq[Currency]]
//    client.get(key).asInstanceOf[Seq[Currency]]
//    
//    var myObj: AnyRef = null
//    var f: java.util.concurrent.Future[Object] = client.asyncGet(key)
//    try {
//      myObj = f.get(20, java.util.concurrent.TimeUnit.SECONDS)
//      myObj.asInstanceOf[Seq[Currency]]
//    } catch {
//      // Since we don't need this, go ahead and cancel the operation.  This
//      // is not strictly necessary, but it'll save some work on the server.
//      case e: Exception => f.cancel(false); null
//      // Do other timeout related stuff
//    }
  }
  
  def clear = {
    client.flush()
  }
  
//  def isClose: Boolean = {
//    (client == null)
//  }

}

 