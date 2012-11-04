package com.oyvindio.sh.modules

import akka.actor.ActorPath
import com.oyvindio.sh.events._
import bot.Op
import util.parsing.combinator.RegexParsers
import com.oyvindio.sh.Scalahugs
import com.typesafe.config.ConfigException
import scala.collection.JavaConversions._
import com.oyvindio.sh.events.Action
import com.oyvindio.sh.events.PrivMsg


class Opers(botPath: ActorPath) extends AbstractScalahugsActor(botPath) {

  protected def receive = {
    case m: PrivMsg => {
      if (shouldOp(m)) op(m)
    }
    case a: Action => {
      if (shouldOp(a)) op(a)
    }
    case t: Trigger if t.trigger == "addop" => {
      // add oper
    }
  }

  private def shouldOp(event: IrcEvent): Boolean = {
    val operHostmasks = try {
      Scalahugs.config.getStringList("sh.opers.%s".format(event.channel.replaceAllLiterally("#",""))).toList
    } catch {
      case e: ConfigException.Missing => List[String]()
    }

    val hostmask = operHostmasks.find(event.matches(_))
    if (hostmask.isDefined) {
      log.debug("Found %s for %s in channel %s".format(hostmask.get, event.hostmask, event.channel))

    } else {
      log.debug("No matches for %s in channel %s".format(event.hostmask, event.channel))
    }

    hostmask.isDefined
  }

  private def op(event: IrcEvent) {
    bot ! Op(event.channel, event.nick)
  }



}