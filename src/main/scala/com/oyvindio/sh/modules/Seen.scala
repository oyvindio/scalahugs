package com.oyvindio.sh.modules

import akka.actor.ActorPath
import com.oyvindio.sh.events._
import bot.BotMsg
import com.oyvindio.sh.events.PrivMsg
import akka.pattern.{AskTimeoutException, AskSupport}
import akka.util.Timeout
import akka.util.duration._

class Seen(botPath: ActorPath) extends AbstractScalahugsActor(botPath) with AskSupport {
  implicit val timeout = Timeout(5 seconds)

  protected def receive = {
    case msg: PrivMsg => {
      if (msg.message.startsWith("!seen ")) {
        val tokens = msg.message.split(' ')
        if (tokens.length < 2) {
          bot ! new BotMsg(msg.channel, "Usage: !seen NICK")
        } else {
          val nick = tokens(1)
          (bot ? 'allNicks).mapTo[Map[String, List[String]]] onComplete {
            case Right(allNicks) => {
              allNicks.get(msg.channel) match {
                case Some(nicks) if nicks.contains(nick) => privMsg(msg.channel, "%s: %s is here right now!".format(msg.nick, nick))
                case _ => privMsg(msg.channel, lookForActivityFromUser(msg.nick, nick))
              }
            }
            case Left(exception) if exception.isInstanceOf[AskTimeoutException] => {
              privMsg(msg.channel, lookForActivityFromUser(msg.nick, nick))
            }
            case Left(exception) => log.error(exception,
              "Exception while checking if '%s' is currently in '%s'!".format(nick, msg.channel))
          }
        }
      }
    }
  }

  private def lookForActivityFromUser(sender: String, nick: String): String = {
    val event = mostRecentEvent(findEventsForNick(nick))
    event.get match {
      case p: PrivMsg => "%s: %s was last seen at %s, saying '%s'.".format(
        sender, p.nick, p.timestampAsLocalTime, p.message)
      case a: Action => "%s: %s was last seen at %s, '* %s'.".format(
        sender, a.nick, a.timestampAsLocalTime, a.action)
      case _ => "I haven't seen %s yet.".format(nick)
    }
  }

  private def findEventsForNick(nick: String): List[IrcEvent] = {
    List(
      PrivMsgDAO.findMostRecentPrivMsg(nick),
      ActionDAO.findMostRecentAction(nick)
    ).flatten
  }

  private def mostRecentEvent(events: List[IrcEvent]): Option[IrcEvent] = {
    if (!events.isEmpty) {
      Some(events.reduce((a, b) => if (a.timestampAsUTC.isAfter(b.timestampAsUTC)) a else b))
    } else {
      None
    }
  }
}