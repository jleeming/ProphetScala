package code.lib.cache

import code.model.Currency
import net.spy.memcached.MemcachedClient
import java.net.InetSocketAddress
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit 

object CacheManager {
  
  private val EXPIRE: Int = 3600 * 24 * 7;
  private val WAIT: Long = 5000;
  
  var client: MemcachedClient = null
  
  def init = {
    val server: String = net.liftweb.util.Props.get("memcachedURL", "localhost")
    val port: Int = net.liftweb.util.Props.getInt("memcachedPort", 11211) 
    client = new MemcachedClient(new InetSocketAddress(server, port));
    println("CacheManager start")
  }
  
  def shutdown = {
    client.shutdown()
  }
  
  def set(key: String, currencyList: Seq[Currency]) = {
    if (!isClose) client.set(key, EXPIRE, currencyList)
  }

  def get(key: String): Seq[Currency] = {
    if (isClose) null
    //client.get(key).asInstanceOf[Seq[Currency]]
    var myObj: Any = null;
    var f: Future[Object] = client.asyncGet("someKey");
    try {
      myObj = f.get(WAIT, TimeUnit.MILLISECONDS)
      myObj.asInstanceOf[Seq[Currency]]
    } catch {
      // Since we don't need this, go ahead and cancel the operation.  This
      // is not strictly necessary, but it'll save some work on the server.
      case e: Exception => {
        f.cancel(false);
        null
      }
      // Do other timeout related stuff      
    }
  }
  
  def clear = {
    client.flush()
  }
  
  def isClose: Boolean = {
    (client == null)
  }

}

 