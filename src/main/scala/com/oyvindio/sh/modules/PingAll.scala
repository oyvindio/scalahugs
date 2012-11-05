package com.oyvindio.sh.modules

import akka.actor.ActorPath
import com.oyvindio.sh.events.Trigger
import com.oyvindio.sh.events.bot.Users
import akka.pattern.AskSupport
import org.jibble.pircbot.User
import akka.util.Timeout
import akka.util.duration._


class PingAll(botpath: ActorPath) extends AbstractScalahugsActor(botpath) with AskSupport {
  implicit val timeout = Timeout(5 seconds)

  protected def receive = {
    case trigger: Trigger if trigger.trigger == "pingall" => {
      (bot ? Users(trigger.channel)).mapTo[List[User]] onSuccess {
        case users: List[User] => privMsg(trigger.channel,
          "%s: %s is trying to reach you!".format(users.map(_.getNick).mkString(", "),
                                                  trigger.nick))
      }
    }
  }
}