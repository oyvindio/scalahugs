package com.oyvindio.sh.modules

import akka.actor.ActorPath
import com.oyvindio.sh.events._
import bot.{Users, BotMsg}
import com.oyvindio.sh.events.PrivMsg
import akka.pattern.{AskTimeoutException, AskSupport}
import akka.util.Timeout
import akka.util.duration._
import com.oyvindio.sh.dao.{ActionDAO, PrivMsgDAO}
import org.jibble.pircbot.User

class Seen(botPath: ActorPath) extends AbstractScalahugsActor(botPath) with AskSupport {
  implicit val timeout = Timeout(5 seconds)

  protected def receive = {
    case msg: Trigger if !msg.hasArgs => bot ! new BotMsg(msg.channel, "Usage: !seen NICK")
    case msg: Trigger if msg.hasArgs && msg.trigger == "seen" => {
      val nick = msg.args.head
      (bot ? Users(msg.channel)).mapTo[List[User]] onComplete {
        case Right(users) => {
          val user = users.find(_.getNick.equals(nick))
          if (user.isDefined) {
            privMsg(msg.channel, "%s: %s is here right now!".format(msg.nick, nick))
          } else {
            privMsg(msg.channel, lookForActivityFromUser(msg.nick, nick))
          }
        }
        case Left(exception) if exception.isInstanceOf[AskTimeoutException] => {
          print("timeout")
          privMsg(msg.channel, lookForActivityFromUser(msg.nick, nick))
        }
        case Left(exception) => log.error(exception,
          "Exception while checking if '%s' is currently in '%s'!".format(nick, msg.channel))
      }
    }
  }

  private def lookForActivityFromUser(sender: String, nick: String): String = {
    val event = mostRecentEvent(findEventsForNick(nick))
    event match {
      case Some(p: PrivMsg) => "%s: %s was last seen at %s, saying '%s'.".format(
        sender, p.nick, p.timestampAsLocalTime, p.message)
      case Some(a: Action) => "%s: %s was last seen at %s, '* %s'.".format(
        sender, a.nick, a.timestampAsLocalTime, a.action)
      case _ => "I haven't seen %s yet.".format(nick)
    }
  }

  private def findEventsForNick(nick: String): List[IrcEvent] = {
    List(
      PrivMsgDAO.findMostRecentPrivMsgFor(nick),
      ActionDAO.findMostRecentActionFor(nick)
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