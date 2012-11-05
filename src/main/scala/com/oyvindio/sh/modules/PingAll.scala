package com.oyvindio.sh.modules

import akka.actor.ActorPath
import com.oyvindio.sh.events.Trigger
import com.oyvindio.sh.events.bot.Users
import com.oyvindio.sh.events.bot.Nick
import akka.pattern.AskSupport
import org.jibble.pircbot.User
import akka.util.Timeout
import akka.util.duration._
import akka.dispatch.Await


class PingAll(botpath: ActorPath) extends AbstractScalahugsActor(botpath) with AskSupport {
  implicit val timeout = Timeout(5 seconds)

  protected def receive = {
    case trigger: Trigger if trigger.trigger == "pingall" => {
      (bot ? Users(trigger.channel)).mapTo[List[User]] onSuccess {
        case users: List[User] => {
          val future = (bot ? new Nick).mapTo[String] onSuccess {
            case botNick: String => botNick
          }
          val excluded = List(Await.result(future, 50 millis), trigger.nick)
          val nicks = users.map(_.getNick).filterNot(excluded contains _)
          privMsg(trigger.channel,
            "%s: %s is trying to reach you!".format(nicks.mkString(", "),
              trigger.nick))
        }
      }
    }
  }
}