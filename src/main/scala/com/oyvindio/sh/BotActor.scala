package com.oyvindio.sh

import akka.actor.{ActorLogging, Actor}
import events.bot.{Nick, Users, Op, BotMsg}
import scala.collection.JavaConversions._

class BotActor(bot: Scalahugs) extends Actor with ActorLogging {

  protected def receive = {
    case msg: BotMsg => bot.sendMessage(msg.channel, msg.message)
    case op: Op => {
      val user = bot.getUsers(op.channel).find(_.getNick.equals(op.nick))
      if (user.isDefined && !user.get.isOp) bot.op(op.channel, op.nick)
    }
    case users: Users => sender ! bot.getUsers(users.channel).toList
    case nick: Nick => sender ! bot.getNick
    case _ => log.warning("Got unexpected message!")
  }
}
