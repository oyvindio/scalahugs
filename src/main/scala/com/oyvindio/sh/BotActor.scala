package com.oyvindio.sh

import akka.actor.{ActorLogging, Actor}
import events.bot.BotMsg

class BotActor(bot: Scalahugs) extends Actor with ActorLogging {

  def allNicks: Map[String, List[String]] = {
    bot.getChannels.map {
      channel => (channel, bot.getUsers(channel).map(user => user.getNick).toList)
    }.toMap
  }

  protected def receive = {
    case msg: BotMsg => bot.sendMessage(msg.channel, msg.message)
    case sym: Symbol => sym match {
      case 'allNicks => sender ! allNicks
      case _ => throw new UnsupportedOperationException("No operation for symbol '%s'".format(sym.toString()))
    }
    case _ => log.warning("Got unexpected message!")
  }
}
