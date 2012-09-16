package com.oyvindio.sh.modules

import akka.actor.{ActorPath, Actor, ActorLogging}
import com.oyvindio.sh.events.bot.BotMsg

abstract class AbstractScalahugsActor(botPath: ActorPath) extends Actor with ActorLogging {
  protected lazy val bot = context.system.actorFor(botPath)

  def privMsg(channel: String, msg: String) {
    bot ! new BotMsg(channel, msg)
  }
}