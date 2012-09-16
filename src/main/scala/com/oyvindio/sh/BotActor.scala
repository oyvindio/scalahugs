package com.oyvindio.sh

import akka.actor.{ActorLogging, Actor}
import events.bot.BotMsg
import events.bot.requests.GetAllNicks

class BotActor(bot: Scalahugs) extends Actor with ActorLogging {

  def allNicks: Map[String, List[String]] = {
    bot.getChannels.map {
      channel => (channel, bot.getUsers(channel).map(user => user.getNick).toList)
    }.toMap
  }

  protected def receive = {
    case msg: BotMsg => bot.sendMessage(msg.channel, msg.message)
    case _: GetAllNicks => sender ! allNicks
    case _ => log.warning("Got unexpected message!")
  }
}