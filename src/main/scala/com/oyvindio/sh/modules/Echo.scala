package com.oyvindio.sh.modules

import akka.actor.{ActorPath, ActorSystem, ActorLogging, Actor}
import com.oyvindio.sh.PrivMsg

class Echo(botPath: ActorPath) extends ScalahugsActor(botPath) {
  protected def receive = {
    case msg: PrivMsg => {
      log.info(msg.toString)
      BOT ! msg
    }
    case _ => log.warning("unexpected message")
  }
}