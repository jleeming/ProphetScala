package code.lib.cache

import net.rubyeye.xmemcached.MemcachedClient
import net.rubyeye.xmemcached.XMemcachedClient
import code.model.Currency

object CacheManager {
  
  private val EXPIRE: Int = 3600 * 24 * 7;
  private val WAIT: Long = 500;
  
  var client: MemcachedClient = null
  
  def init = {
    val server: String = net.liftweb.util.Props.get("memcachedURL", "localhost")
    val port: Int = net.liftweb.util.Props.getInt("memcachedPort", 11211) 
    client = new XMemcachedClient(server, port)
    println("CacheManager start")
  }
  
  def shutdown = {
    client.shutdown()
  }
  
  def set(key: String, currencyList: Seq[Currency]) = {
    if (!isClose) client.set(key, EXPIRE, currencyList, WAIT)
  }
  
  def get(key: String): Seq[Currency] = {
    if (isClose) null
    client.get(key)
  }
  
  def clear = {
    client.flushAll()
  }
  
  def isClose: Boolean = {
    (client == null) || (client.isShutdown())
  }

}

 