package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._

import code.lib._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    
    code.lib.cache.CacheManager.init
    
    // where to search snippet
    LiftRules.addToPackages("code")

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.statelessDispatch.append(FullRest)

    // stateful versions of the above
    // LiftRules.dispatch.append(FullRest)

  }
}
