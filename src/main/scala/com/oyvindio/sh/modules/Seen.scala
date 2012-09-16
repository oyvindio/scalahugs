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

          val getNicksAsync = bot ? 'allNicks
          getNicksAsync onSuccess {
            case allUsers: Map[String, List[String]] => {
              val usersInChannel = allUsers.get(msg.channel)
              val reply = if (usersInChannel.isDefined && usersInChannel.get.contains(nick)) {
                "%s: %s is here right now!".format(msg.nick, nick)
              } else {
                lookupNickInDatabase(msg.nick, nick)
              }

              privMsg(msg.channel, reply)
            }
          }

          getNicksAsync onFailure {
            case timeout: AskTimeoutException => {
              privMsg(msg.channel, lookupNickInDatabase(msg.nick, nick))
            }
            case e: Exception => log.error(e,
                          "Exception while checking if '%s' is currently in '%s'!".format(nick, msg.channel))
          }
        }
      }
    }
  }

  def lookupNickInDatabase(sender: String, nick: String): String = {
    mostRecentEvent(findEventsForNick(nick)) match {
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