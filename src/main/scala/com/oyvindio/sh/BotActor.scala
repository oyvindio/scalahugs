package com.oyvindio.sh

import akka.actor.{ActorLogging, Actor}
import events.bot.{Op, BotMsg}

class BotActor(bot: Scalahugs) extends Actor with ActorLogging {

  def allNicks: Map[String, List[String]] = {
    bot.getChannels.map {
      channel => (channel, bot.getUsers(channel).map(user => user.getNick).toList)
    }.toMap
  }

  protected def receive = {
    case msg: BotMsg => bot.sendMessage(msg.channel, msg.message)
    case op: Op => {
      val user = bot.getUsers(op.channel).find(_.getNick.equals(op.nick))
      if (user.isDefined && !user.get.isOp) bot.op(op.channel, op.nick)
    }
    case sym: Symbol => sym match {
      case 'allNicks => sender ! allNicks
      case _ => throw new UnsupportedOperationException("No operation for symbol '%s'".format(sym.toString()))
    }
    case _ => log.warning("Got unexpected message!")
  }
}
