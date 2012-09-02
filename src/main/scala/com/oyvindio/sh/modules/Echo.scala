package com.oyvindio.sh.modules

import akka.actor.{ActorPath, ActorSystem, ActorLogging, Actor}
import com.oyvindio.sh.PrivMsg

class Echo(botPath: ActorPath) extends Actor with ActorLogging {
  private lazy val bot = context.system.actorFor(botPath)

  protected def receive = {
    case msg: PrivMsg => {
      log.info(msg.toString)
      bot ! msg
    }
    case _ => log.warning("unexpected message")
  }
}